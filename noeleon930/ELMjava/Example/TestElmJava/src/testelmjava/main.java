package testelmjava;

import no.uib.cipr.matrix.NotConvergedException;

public class main
{

    /**
     * @param args
     * @throws NotConvergedException
     */
    public static void main(String[] args) throws NotConvergedException
    {
        elm ds = new elm(1, 1000, "sig");
        ds.train("kdd_feature_train_java");
//        ds.test("diabetes_test");

        System.out.println("TrainingTime:" + ds.getTrainingTime());
        System.out.println("TrainingAcc:" + ds.getTrainingAccuracy());
//        System.out.println("TestingTime:" + ds.getTestingTime());
//        System.out.println("TestAcc:" + ds.getTestingAccuracy());

//        elm ds1 = new elm(1, 1000, "sin");
//        ds1.train("diabetes_train");
//        ds1.test("diabetes_test");
//
//        System.out.println("TrainingTime:" + ds1.getTrainingTime());
//        System.out.println("TrainingAcc:" + ds1.getTrainingAccuracy());
//        System.out.println("TestingTime:" + ds1.getTestingTime());
//        System.out.println("TestAcc:" + ds1.getTestingAccuracy());
    }

}
