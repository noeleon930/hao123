import numpy
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.cross_validation import cross_val_score

lag = 10

print "Loading train_data..."
train_data = numpy.loadtxt("../KddJavaToolChain/train_timelines_final.csv",
						   delimiter=",")
print "Loading train_data completed..."

print "Loading test_data..."
test_data = numpy.loadtxt("../KddJavaToolChain/predict_timelines.csv",
						  delimiter=",")
print "Loading test_data completed..."

print "Loading truth_data..."
truth_data_lines = open("../KddJavaToolChain/truth_timelines_final.csv", "r").readlines()
truth_data = []
for truth_data_line in truth_data_lines:
	truth_data.append(int(truth_data_line.replace("\n", "")))
print "Loading truth_data completed..."

for i in range(501,1001,50):
	n = 1000
	# i = 420
	clf_rf = RandomForestClassifier(n_estimators=n, n_jobs=-1, max_depth=None, min_samples_split=i, verbose=False)
	scores = cross_val_score(clf_rf, train_data, truth_data, n_jobs=1, cv=2, verbose=False)
	print i, ":", scores.mean()

# clf = GradientBoostingClassifier(verbose=False)
# clf.fit(train_data, truth_data)
#
# index = [i for i in range(lag)]
# zipped = zip(index, clf.feature_importances_)
# zipped.sort(key = lambda t: t[1], reverse=True)
#
# for i, j in zipped:
#     print i, ":", j
