import os
import logging
import traceback
from flask import Flask, request, jsonify
import numpy as np
import requests
from CustomDecisionTree import CustomDecisionTree as cdt

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = Flask(__name__)

# Global variable to store the trained model
trained_model = None

JAVA_API_BASE_URL = os.getenv("JAVA_API_BASE_URL", "http://localhost:8080") + "/api"

def get_mbti_types():
    """
    Fetch MBTI personality types from the Java backend API.

    Returns:
        list: Available MBTI types
    """
    try:
        logger.info("Fetching MBTI types from API")
        response = requests.get(f"{JAVA_API_BASE_URL}/enums/mbti")
        response.raise_for_status()
        return response.json()
    except requests.RequestException as e:
        logger.error(f"Error fetching MBTI types: {str(e)}")
        raise Exception(f"Failed to retrieve MBTI types: {str(e)}")


def get_questions():
    """
    Fetch personality questions from the Java backend API.

    Returns:
        list: Available personality questions
    """
    try:
        logger.info("Fetching questions from API")
        response = requests.get(f"{JAVA_API_BASE_URL}/questions")
        response.raise_for_status()
        return response.json()
    except requests.RequestException as e:
        logger.error(f"Error fetching questions: {str(e)}")
        raise Exception(f"Failed to retrieve questions: {str(e)}")


def get_all_answers():
    """
    Fetch all recorded answers from the Java backend API.

    Returns:
        list: All user answers with corresponding MBTI types
    """
    try:
        logger.info("Fetching answers from API")
        response = requests.get(f"{JAVA_API_BASE_URL}/answers")
        response.raise_for_status()
        return response.json()
    except requests.RequestException as e:
        logger.error(f"Error fetching answers: {str(e)}")
        raise Exception(f"Failed to retrieve answers: {str(e)}")


def prepare_dataset():
    """
    Prepare training dataset from API data.

    Returns:
        tuple: (X, y, mbti_types) where X is features, y is labels,
               and mbti_types is the list of possible types
    """
    try:
        logger.info("Preparing dataset for model training")
        raw_data = get_all_answers()
        mbti_types = get_mbti_types()

        X = []
        y = []

        for entry in raw_data:
            answers = [1 if ans["yes"] else 0 for ans in entry["answers"]]
            X.append(answers)
            y.append(mbti_types.index(entry["mbtiType"]))

        logger.info(f"Dataset prepared: {len(X)} samples, {len(mbti_types)} MBTI types")
        return np.array(X), np.array(y), mbti_types
    except Exception as e:
        logger.error(f"Error preparing dataset: {str(e)}")
        raise


@app.route('/api/mbti/train', methods=['POST'])
def train():
    """
    Train the MBTI prediction model with available data.
    Accepts a 'depth' parameter to configure tree depth.

    Returns:
        JSON response indicating training status
    """
    global trained_model

    try:
        data = request.json
        if not data:
            logger.warning("No JSON data in request")
            return jsonify({"error": "Missing request data"}), 400

        depth = data.get('depth', 5)
        logger.info(f"Training model with depth={depth}")

        X, y, _ = prepare_dataset()

        if len(X) == 0:
            logger.warning("Empty training dataset")
            return jsonify({"error": "No training data available"}), 400

        trained_model = cdt(max_depth=depth)
        trained_model.fit(X, y)

        logger.info(f"Model trained successfully with {len(X)} samples")
        return jsonify({"status": "success", "message": f"Model trained with depth {depth}"})

    except Exception as e:
        error_msg = f"Error training model: {str(e)}"
        logger.error(error_msg)
        logger.error(traceback.format_exc())
        return jsonify({"error": error_msg}), 500


@app.route('/api/mbti/next', methods=['POST'])
def get_next_question_or_result():
    """
    Process current answers and determine either:
    - The next question to ask
    - The final MBTI prediction

    Requires the 'answers' array in the request body.

    Returns:
        JSON with either the next question or final MBTI result
    """
    global trained_model

    try:
        if trained_model is None:
            logger.warning("API call before model training")
            return jsonify({"error": "Model not trained"}), 400

        if not request.json:
            logger.warning("No JSON data in request")
            return jsonify({"error": "Missing request data"}), 400

        mbti_types = get_mbti_types()
        questions = get_questions()

        answers = request.json.get("answers", [])
        current_node = trained_model.root

        logger.info(f"Processing answers: {answers}")

        # Navigate the decision tree based on answers
        for i, answer in enumerate(answers):
            if current_node.prediction is not None:
                mbti_type = mbti_types[current_node.prediction]
                logger.info(f"Early prediction reached: {mbti_type}")
                return jsonify({"mbti": mbti_type})

            # Take left path for Yes (1), right path for No (0)
            current_node = current_node.left if answer else current_node.right
            logger.debug(f"Answer {i}: {answer}, moving to {'left' if answer else 'right'} node")

        # Check if we've reached a leaf node (prediction)
        if current_node.prediction is not None:
            mbti_type = mbti_types[current_node.prediction]
            logger.info(f"Final prediction: {mbti_type}")
            return jsonify({"mbti": mbti_type})
        else:
            feature_index = current_node.feature_index
            if feature_index is None or feature_index >= len(questions):
                error_msg = f"Invalid feature index: {feature_index}"
                logger.error(error_msg)
                return jsonify({"error": error_msg}), 500

            question = questions[feature_index]
            logger.info(f"Next question: {question['id']}")
            return jsonify({
                "questionId": question["id"],
                "questionText": question["content"]
            })

    except Exception as e:
        error_msg = f"Error processing request: {str(e)}"
        logger.error(error_msg)
        logger.error(traceback.format_exc())
        return jsonify({"error": error_msg}), 500


if __name__ == '__main__':
    logger.info("Starting MBTI Prediction Service")
    app.run(host='0.0.0.0', port=5000)