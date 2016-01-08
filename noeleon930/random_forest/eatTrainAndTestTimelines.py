train_timelines = open("../KddJavaToolChain/train_timelines.csv", 'r').readlines()
test_timelines = open("../KddJavaToolChain/test_timelines.csv", 'r').readlines()

train_timelines_final = open("../KddJavaToolChain/train_timelines_final.csv", 'w')
truth_timelines_final = open("../KddJavaToolChain/truth_timelines_final.csv", 'w')

train_data = []
truth_data = []

for timeline in train_timelines:
    tmp = timeline.replace("\n", "")
    train_part = tmp.split("->")[0]
    truth_part = tmp.split("->")[1]
    train_data.append(train_part)
    truth_data.append(truth_part)

for timeline in test_timelines:
    tmp = timeline.replace("\n", "")
    train_part = tmp.split("->")[0]
    truth_part = tmp.split("->")[1]
    train_data.append(train_part)
    truth_data.append(truth_part)

for data in train_data:
    train_timelines_final.write(data + "\n")

for truth in truth_data:
    truth_timelines_final.write(truth + "\n")

train_timelines_final.close()
truth_timelines_final.close()
