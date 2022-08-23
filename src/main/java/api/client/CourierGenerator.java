package api.client;

public class CourierGenerator {
    public static CreateCourier getDefault(){
        return new CreateCourier ("Oksana24", "123456","Nikita");
    }
    public static LoginCourier loginCourierAfterCreated(){
        return new LoginCourier ("Oksana24","123456");
    }
    public static LoginCourier loginCourierForTest(){
        return new LoginCourier("Grigoriy38", "12345");
    }

}
