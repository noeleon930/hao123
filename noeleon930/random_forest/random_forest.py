__author__ = 'Noel'

import numpy
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import ExtraTreesClassifier
from sklearn.cross_validation import cross_val_score

# objSerialize = open('clf.obj', 'wb')

# total 245
# usingCols = tuple([i for i in range(245) if i not in [ii for ii in range(9, 24)]])
# usingCols = tuple([0, 1, 2, 3, 4, 5, 6, 7, 8, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 46, 47, 48])
usingCols = tuple([i for i in range(48) if i not in [9,11,12,13,14,17,18,21,22,23]])

print "using cols:"
print usingCols

print "Loading train_data..."
train_data = numpy.loadtxt("../KddJavaToolChain/train_feature.csv",
						   delimiter=",",
						   skiprows=1,
						   usecols=usingCols)
print "Loading train_data completed..."

# print "Loading train_timeseries_data..."
# train_timeseries_data = numpy.loadtxt("../KddJavaToolChain/train_feature_timeseries.csv",
# 						   delimiter=",",
# 						   skiprows=0)
# print "Loading train_timeseries_data completed..."

print "Loading test_data..."
test_data = numpy.loadtxt("../KddJavaToolChain/test_feature.csv",
						  delimiter=",",
						  skiprows=1,
						  usecols=usingCols)
print "Loading test_data completed..."

# print "Loading test_timeseries_data..."
# test_timeseries_data = numpy.loadtxt("../KddJavaToolChain/test_feature_timeseries.csv",
# 						  delimiter=",",
# 						  skiprows=0)
# print "Loading test_timeseries_data completed..."

print "Loading truth_data..."
truth_data_lines = open("../../data_kdd/train/truth_train.csv", "r").readlines()
truth_data = []
for truth_data_line in truth_data_lines:
	truth_data.append(int(truth_data_line.replace("\n", "").split(",")[1]))
print "Loading truth_data completed..."

# mix!
# if(len(train_data) != len(train_timeseries_data)):
# 	print "fuck!"
# if(len(test_data) != len(test_timeseries_data)):
# 	print "fuck!"
# for i in range(len(train_data)):
# 	train_data[i].extend(train_timeseries_data[i])
# for i in range(len(test_data)):
# 	test_data[i].extend(test_timeseries_data[i])

for i in range(50, 1201, 100):
# i = 200
	clf_rf = RandomForestClassifier(n_estimators=40000, n_jobs=-1, max_depth=None, min_samples_split=i, verbose=True)
	scores = cross_val_score(clf_rf, train_data, truth_data, n_jobs=1, cv=2, verbose=False)
	print i, ":", scores.mean()

# clf_rf.fit(train_data, truth_data)
# predicts = clf_rf.predict_proba(test_data)

# print "Writing to csv file..."
# predicts_output = open("20000_400.csv", 'w')
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
