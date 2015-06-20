import numpy
# from sklearn.linear_model import SGDClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.cross_validation import cross_val_score

# (zero base!)
# total 245
usingCols = tuple([i for i in range(245) if i not in [ii for ii in range(9, 24)]])
# usingCols = tuple([0, 1, 2, 3, 4, 5, 6, 7, 8, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 46, 47, 48])
# usingCols = tuple([i for i in range(245)])

print "using cols:"
print usingCols

# do forget to check skiprows!
print "Loading train_data..."
train_data = numpy.loadtxt("../KddJavaToolChain/train_feature_raw_and_timeseries.csv",
						   delimiter=",",
						   skiprows=0,
						   usecols=usingCols)
print "Loading train_data completed..."

print "Loading test_data..."
test_data = numpy.loadtxt("../KddJavaToolChain/test_feature_raw_and_timeseries.csv",
						  delimiter=",",
						  skiprows=0,
						  usecols=usingCols)
print "Loading test_data completed..."

print "Loading truth_data..."
truth_data_lines = open("../../data_kdd/train/truth_train.csv", "r").readlines()
truth_data = []
for truth_data_line in truth_data_lines:
	truth_data.append(int(truth_data_line.replace("\n", "").split(",")[1]))
print "Loading truth_data completed..."

# Scaling!
# scaler = StandardScaler()
# scaler.fit(train_data)
# train_data = scaler.transform(train_data)
# test_data = scaler.transform(test_data)

# Fitting!
clf_knn = KNeighborsClassifier(n_neighbors=101)
# scores = cross_val_score(clf_knn, train_data, truth_data, n_jobs=1, cv=5, verbose=True)
# print scores.mean()
clf_knn.fit(train_data, truth_data)
predicts = clf_knn.predict_proba(test_data)

print "Writing to csv file..."
predicts_output = open("knn.csv", 'w')
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
