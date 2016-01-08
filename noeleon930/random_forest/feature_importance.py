import numpy
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier

usingCols = tuple([i for i in range(106)])

print "using cols:"
print usingCols

print "Loading train_data..."
train_data = numpy.loadtxt("../KddJavaToolChain/train_feature.csv",
						   delimiter=",",
						   skiprows=2,
						   usecols=usingCols)
print "Loading train_data completed..."

print "Loading test_data..."
test_data = numpy.loadtxt("../KddJavaToolChain/test_feature.csv",
						  delimiter=",",
						  skiprows=2,
						  usecols=usingCols)
print "Loading test_data completed..."

print "Loading truth_data..."
truth_data_lines = open("../../data_kdd/train/truth_train.csv", "r").readlines()
truth_data = []
for truth_data_line in truth_data_lines:
	truth_data.append(int(truth_data_line.replace("\n", "").split(",")[1]))
print "Loading truth_data completed..."

clf = RandomForestClassifier(n_estimators=2000, n_jobs=-1, max_depth=None, verbose=False)
clf.fit(train_data, truth_data)

index = [i for i in range(len(usingCols))]
zipped = zip(index, clf.feature_importances_)
zipped.sort(key = lambda t: t[1], reverse=True)

for i, j in zipped:
    print i, ":", j
