import numpy
from sklearn.ensemble import RandomForestClassifier
from sklearn.cross_validation import cross_val_score

# objSerialize = open('clf.obj', 'wb')

# total 245
# usingCols = tuple([i for i in range(245) if i not in [ii for ii in range(9, 24)]])
# usingCols = tuple([0, 1, 2, 3, 4, 5, 6, 7, 8, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 46, 47, 48])
usingCols = tuple([i for i in range(186) if i not in [77,67,27,149,73,97,19,62,64,23,152,61,37,71,156,49,163,5,82,109,69,16,124,158,159,181,8,101,123,30,9,185,17,22,138,129,90,40,10,154,128,120,41,135,53,127,68,12,126,137,176,115,38,167,26,2,161,102,160,183,24,110,118,114,93,113,112,121,133,78,0,1,3,4,6,7,11,13,14,15,18,20,25,28,29,31,32,34,36,39,43,44,45,46,47,48,50,51,52,54,55,56,57,59,60,63,65,70,72,75,79,80,81,83,84,88,89,91,92,99,100,107,108,111,116,117,119,122,125,130,131,132,134,140,141,142,143,144,145,146,147,148,150,151,155,157,162,164,165,166,168,169,170,171,173,175,178,179,180,182,184]])

print "using cols:"
print usingCols

print "Loading train_data..."
train_data = numpy.loadtxt("../KddJavaToolChain/train_feature.csv",
						   delimiter=",",
						   skiprows=2,
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
						  skiprows=2,
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

# thresh < 0.01 -> 111
# thresh < -> 71

# for i in range(1,502,10):
n = 20000
i = 91
clf_rf = RandomForestClassifier(n_estimators=n, n_jobs=7, max_depth=None, min_samples_split=i, verbose=False)
	# scores = cross_val_score(clf_rf, train_data, truth_data, n_jobs=1, cv=2, verbose=False)
	# print "2000es, split", i, ":", scores.mean()
#
clf_rf.fit(train_data, truth_data)
predicts = clf_rf.predict_proba(test_data)

print "Writing to csv file..."
predicts_output = open("186dlog_20000_91_selected_thresh001_gradient.csv", 'w')
sample_submission = open("../../data_kdd/sampleSubmission.csv", 'r').readlines()
sample_submission_lines = []
for line in sample_submission:
	sample_submission_lines.append(line.replace("\n", "").split(",")[0])

if len(sample_submission_lines) != len(predicts):
	print "fuck!"

for lineNum in range(len(sample_submission_lines)):
	out = sample_submission_lines[lineNum] + "," + str(predicts[lineNum][1]) + "\n"
	out = out.replace(".\n", ".0\n").replace(",1.0\n", ",0.999\n").replace(",0.0\n", ",0.001\n")
	predicts_output.write(out)
predicts_output.close()
print "Writing to csv file completed..."
