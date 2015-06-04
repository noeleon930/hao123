using ELM

X_train = [1 1; 0 0; 1 0; 0 1]
Y_train = [0, 0, 1, 1]

elm = ExtremeLearningMachine(2500) # number of nodes
fit!(elm, float(X_train), float(Y_train))

X_test = [1 1; 0 0; 1 0; 0 1]
println(predict(elm, float(X_test)))
