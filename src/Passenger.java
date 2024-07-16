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

    String[] toDivide;

    public Passenger(String info) {
        toDivide = info.split(",");
        try {
            passengerId = Integer.parseInt(toDivide[0]);
            survived = Integer.parseInt(toDivide[1]);
            pClass = Integer.parseInt(toDivide[2]);
            name = toDivide[3] + toDivide[4];
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

    public String getFormattedName() {
        String formattedName = toDivide[4] + toDivide[3];
        int dotIndex = formattedName.indexOf(".");
        formattedName = formattedName.substring(dotIndex + 1);
        formattedName = formattedName.replace("\"", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
        return formattedName;
    }

}
