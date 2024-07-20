public class Passenger {
    private int passengerId;
    private int survived;
    private int pClass;
    private String name;
    private String sex;
    private float age;
    private int sibSp;
    private int parch;
    private String ticket;
    private float fare;
    private String cabin;
    private String embarked;

    String[] toDivide;

    public Passenger(String info) {
        toDivide = info.split(",");
        setPassengerId();
        setSurvived();
        setpClass();
        setName();
        setSex();
        setAge();
        setSibSp();
        setParch();
        setTicket();
        setFare();
        setCabin();
        setEmbarked();
    }

    public void setPassengerId() {
        if (toDivide[0] != null && !toDivide[0].isEmpty()) {
            passengerId = Integer.parseInt(toDivide[0]);
        } else {
            passengerId = 0;
        }
    }

    public void setSurvived() {
        if (toDivide[1] != null && !toDivide[1].isEmpty()) {
            survived = Integer.parseInt(toDivide[1]);
        }
    }

    public void setpClass() {
        if (toDivide[2] != null && !toDivide[2].isEmpty()) {
            pClass = Integer.parseInt(toDivide[2]);
        }
    }

    public void setName() {
        if (toDivide[3] != null && !toDivide[3].isEmpty()) {
            if (toDivide[4] != null && !toDivide[4].isEmpty()) {
                name = toDivide[3] + " " + toDivide[4];
            }
        } else {
            name = "";
        }
    }

    public void setSex() {
        if (toDivide[5] != null && !toDivide[5].isEmpty()) {
            sex = toDivide[5];
        } else {
            sex = "";
        }
    }

    public void setAge() {
        if (toDivide[6] != null && !toDivide[6].isEmpty()) {
            age = Float.parseFloat(toDivide[6]);
        } else {
            age = 0;
        }
    }

    public void setSibSp() {
        if (toDivide[7] != null && !toDivide[7].isEmpty()) {
            sibSp = Integer.parseInt(toDivide[7]);
        } else {
            sibSp = 0;
        }
    }

    public void setParch() {
        if (toDivide[8] != null && !toDivide[8].isEmpty()) {
            parch = Integer.parseInt(toDivide[8]);
        } else {
            parch = 0;
        }
    }

    public void setTicket() {
        if (toDivide[9] != null && !toDivide[9].isEmpty()) {
            ticket = toDivide[9];
        } else {
            ticket = "";
        }
    }

    public void setFare() {
        if (toDivide[10] != null && !toDivide[10].isEmpty()) {
            fare = Float.parseFloat(toDivide[10]);
        } else {
            fare = 0;
        }
    }

    public void setCabin() {
        if (toDivide[11] != null && !toDivide[11].isEmpty()) {
            cabin = toDivide[11];
        } else {
            cabin = "";
        }
    }

    public void setEmbarked() {
        try {
            if (toDivide[12] != null && !toDivide[12].isEmpty()) {
                embarked = toDivide[12];
            } else {
                embarked = "";
            }
        } catch (Exception e) {
            embarked = "";
        }
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getSurvived() {
        return survived;
    }

    public boolean isSurvived() {
        return survived == 1;
    }

    public int getPClass() {
        return pClass;
    }

    public String getClassAsString() {
        String pClass = "";
        switch (this.pClass) {
            case 1 -> pClass = "1st";
            case 2 -> pClass = "2nd";
            case 3 -> pClass = "3rd";
        }
        return pClass;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public float getAge() {
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
        String formattedName = toDivide[4] + " " + toDivide[3];
        int dotIndex = formattedName.indexOf(".");
        formattedName = formattedName.substring(dotIndex + 1);
        formattedName = formattedName.replace("\"", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
        return formattedName;
    }

}
