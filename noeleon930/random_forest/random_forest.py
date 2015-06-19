__author__ = 'Noel'

import numpy
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import ExtraTreesClassifier
from sklearn.cross_validation import cross_val_score

# objSerialize = open('clf.obj', 'wb')

# total 49
# usingCols = tuple([i for i in range(49) if (i not in [ii for ii in range(9, 39)])])
# usingCols = tuple([0, 1, 2, 3, 4, 5, 6, 7, 8, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 46, 47, 48])
usingCols = tuple([i for i in range(196)])

print "using cols:"
print usingCols

print "Loading train_data..."
train_data = numpy.loadtxt("../KddJavaToolChain/train_feature_timeseries.csv",
						   delimiter=",",
						   skiprows=0,
						   usecols=usingCols)
# for arr in train_data:
# 	total = float(sum(arr))
# 	for i in range(len(arr)):
# 		if total > 0.0:
# 			arr[i] = float(arr[i]) / total
# 		else:
# 			arr[i] = 0.0
print "Loading train_data completed..."

print "Loading test_data..."
test_data = numpy.loadtxt("../KddJavaToolChain/test_feature_timeseries.csv",
						  delimiter=",",
						  skiprows=0,
						  usecols=usingCols)
# for arr in test_data:
# 	total = float(sum(arr))
# 	for i in range(len(arr)):
# 		if total > 0.0:
# 			arr[i] = float(arr[i]) / total
# 		else:
# 			arr[i] = 0.0
print "Loading test_data completed..."

print "Loading truth_data..."
truth_data_lines = open("../../data_kdd/train/truth_train.csv", "r").readlines()
truth_data = []
for truth_data_line in truth_data_lines:
	truth_data.append(int(truth_data_line.replace("\n", "").split(",")[1]))
print "Loading truth_data completed..."

clf_rf = ExtraTreesClassifier(n_estimators=3000, n_jobs=4, max_depth=None, min_samples_split=10, min_samples_leaf=10, bootstrap=False, verbose=True)
# clf_rf = clf_rf.fit(train_data, truth_data)
scores = cross_val_score(clf_rf, train_data, truth_data, n_jobs=1, cv=5, verbose=True)
print scores.mean()
print clf_rf.get_params()

# predicts = clf_rf.predict_proba(test_data)

# print "Writing to csv file..."
# predicts_output = open("random_forest.csv", 'w')
# sample_submission = open("../../data_kdd/sampleSubmission.csv", 'r').readlines()
# sample_submission_lines = []
# for line in sample_submission:
# 	sample_submission_lines.append(line.replace("\n", "").split(",")[0])

# if len(sample_submission_lines) != len(predicts):
# 	print "fuck!"

# for lineNum in range(len(sample_submission_lines)):
# 	out = sample_submission_lines[lineNum] + "," + str(predicts[lineNum][1]) + "\n"
# 	out = out.replace(".\n", ".0\n").replace(",1.0\n", ",0.999\n").replace(",0.0\n", ",0.001\n")
# 	predicts_output.write(out)
# predicts_output.close()
# print "Writing to csv file completed..."
