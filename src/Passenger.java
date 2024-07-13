public class Passenger {
    private int passengerId;
    private int survived;
    private int pClass;
    private String name;
    private String sex;
    private int age;
    private int sibSp;
    private int parch;
    private String ticket;
    private float fare;
    private String cabin;
    private String embarked;

    public Passenger(String info){
        String[] toDivide = info.split(",");
        try {
            passengerId = Integer.parseInt(toDivide[0]);
            survived = Integer.parseInt(toDivide[1]);
            pClass = Integer.parseInt(toDivide[2]);
            name = toDivide[3].substring(1) + toDivide[4].substring(0, toDivide[4].length() - 1);
            sex = toDivide[5];
            age = Integer.parseInt(toDivide[6]);
            sibSp = Integer.parseInt(toDivide[7]);
            parch = Integer.parseInt(toDivide[8]);
            ticket = toDivide[9];
            fare = Float.parseFloat(toDivide[10]);
            cabin = toDivide[11];
            embarked = toDivide[12];
        } catch (Exception e) {

        }
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getSurvived() {
        return survived;
    }

    public int getPClass() {
        return pClass;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getSibSp() {
        return sibSp;
    }

    public int getParch() {
        return parch;
    }

    public String getTicket() {
        return ticket;
    }

    public float getFare() {
        return fare;
    }

    public String getCabin() {
        return cabin;
    }

    public String getEmbarked() {
        return embarked;
    }

    public String getFormattedName(){
        int dotIndex = name.indexOf(".");
        int spaceIndex = name.indexOf(" ");
        return name.substring(dotIndex + 2) + " " + name.substring(0, spaceIndex);
    }

//    public String toString(){
//
//    }
}
