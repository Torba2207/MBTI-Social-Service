import numpy as np

class DecisionNode:
    def __init__(self, feature_index=None, threshold=None, left=None, right=None, prediction=None):
        self.feature_index = feature_index  # Index of the feature to split on
        self.threshold = threshold          # Threshold for splitting
        self.left = left                    # Left subtree
        self.right = right                  # Right subtree
        self.prediction = prediction        # Prediction if this is a leaf node


class CustomDecisionTree:
    def __init__(self, max_depth=5):
        self.max_depth = max_depth
        self.root = None

    def fit(self, X, y):
        self.root = self._build_tree(X, y, depth=0)

    def _build_tree(self, X, y, depth):
        # Check stopping conditions
        if len(set(y)) == 1 or depth >= self.max_depth:
            # Create a leaf node with the majority class
            prediction = np.bincount(y).argmax()
            return DecisionNode(prediction=prediction)

        # Find the best feature and threshold to split on
        best_feature, best_threshold = self._find_best_split(X, y)
        if best_feature is None:
            prediction = np.bincount(y).argmax()
            return DecisionNode(prediction=prediction)

        # Split the dataset
        left_indices = X[:, best_feature] == 1
        right_indices = ~left_indices

        # Recursively build the left and right subtrees
        left_subtree = self._build_tree(X[left_indices], y[left_indices], depth + 1)
        right_subtree = self._build_tree(X[right_indices], y[right_indices], depth + 1)

        return DecisionNode(feature_index=best_feature, threshold=best_threshold,
                            left=left_subtree, right=right_subtree)

    def _find_best_split(self, X, y):
        best_feature = None
        best_gain = 0
        n_features = X.shape[1]

        for feature_index in range(n_features):
            left_mask = X[:, feature_index] == 1
            right_mask = ~left_mask

            if np.sum(left_mask) == 0 or np.sum(right_mask) == 0:
                continue

            left_labels = y[left_mask]
            right_labels = y[right_mask]

            # Calculate information gain
            gain = self._information_gain(y, left_labels, right_labels)

            if gain > best_gain:
                best_gain = gain
                best_feature = feature_index

        return best_feature, 0.5  # Threshold is always 0.5 for binary features

    def _information_gain(self, parent, left, right):
        parent_entropy = self._entropy(parent)
        left_entropy = self._entropy(left)
        right_entropy = self._entropy(right)

        left_weight = len(left) / len(parent)
        right_weight = len(right) / len(parent)

        return parent_entropy - (left_weight * left_entropy + right_weight * right_entropy)

    def _entropy(self, labels):
        _, counts = np.unique(labels, return_counts=True)
        probabilities = counts / len(labels)
        return -np.sum(probabilities * np.log2(probabilities))

    def predict(self, X):
        return np.array([self._predict_sample(x, self.root) for x in X])

    def _predict_sample(self, x, node):
        if node.prediction is not None:
            return node.prediction
        if x[node.feature_index] == 1:
            return self._predict_sample(x, node.left)
        else:
            return self._predict_sample(x, node.right)

