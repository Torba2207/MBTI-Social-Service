from flask import Flask, request, jsonify
import numpy as np
import requests
from CustomDecisionTree import CustomDecisionTree as cdt

app = Flask(__name__)

# Global variable to store the trained model
trained_model = None

JAVA_API_BASE_URL = "http://mbti_backend:8080/api"

def get_mbti_types():
    response = requests.get(f"{JAVA_API_BASE_URL}/enums/mbti")
    return response.json()

def get_questions():
    response = requests.get(f"{JAVA_API_BASE_URL}/questions")
    return response.json()

def get_all_answers():
    response = requests.get(f"{JAVA_API_BASE_URL}/answers")
    return response.json()

def prepare_dataset():
    raw_data = get_all_answers()
    mbti_types = get_mbti_types()

    X = []
    y = []

    for entry in raw_data:
        answers = [1 if ans["yes"] else 0 for ans in entry["answers"]]
        X.append(answers)
        y.append(mbti_types.index(entry["mbtiType"]))

    return np.array(X), np.array(y), mbti_types

@app.route('/api/mbti/train', methods=['POST'])
def train():
    global trained_model

    data = request.json
    depth = data.get('depth', 5)

    X, y, _ = prepare_dataset()
    trained_model = cdt(max_depth=depth)
    trained_model.fit(X, y)

    return jsonify({"status": "success", "message": f"Model trained with depth {depth}"})

@app.route('/api/mbti/next', methods=['POST'])
def get_next_question_or_result():
    global trained_model

    if trained_model is None:
        return jsonify({"error": "Model not trained"}), 400

    mbti_types = get_mbti_types()
    questions = get_questions()

    answers = request.json.get("answers", [])
    current_node = trained_model.root

    for answer in answers:
        if current_node.prediction is not None:
            mbti_type = mbti_types[current_node.prediction]
            return jsonify({"mbti": mbti_type})

        current_node = current_node.left if answer else current_node.right

    if current_node.prediction is not None:
        mbti_type = mbti_types[current_node.prediction]
        return jsonify({"mbti": mbti_type})
    else:
        feature_index = current_node.feature_index
        if feature_index is None or feature_index >= len(questions):
            return jsonify({"error": "Invalid feature index"}), 500

        question = questions[feature_index]
        return jsonify({
            "questionId": question["id"],
            "questionText": question["content"]
        })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)