__author__ = 'Noel'

from sklearn.ensemble import RandomForestClassifier

# X = [[0, 0], [1, 1]]
# Y = [0, 1]
clf = RandomForestClassifier(n_estimators=1000, n_jobs=-1)
clf = clf.fit(X, Y)

print clf.predict_proba([1, 0])
