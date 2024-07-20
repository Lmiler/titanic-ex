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
        if (toDivide[Constants.ID_INDEX] != null && !toDivide[Constants.ID_INDEX].isEmpty()) {
            passengerId = Integer.parseInt(toDivide[Constants.ID_INDEX]);
        } else {
            passengerId = 0;
        }
    }

    public void setSurvived() {
        if (toDivide[Constants.SURVIVED_INDEX] != null && !toDivide[Constants.SURVIVED_INDEX].isEmpty()) {
            survived = Integer.parseInt(toDivide[1]);
        }
    }

    public void setpClass() {
        if (toDivide[Constants.CLASS_INDEX] != null && !toDivide[Constants.CLASS_INDEX].isEmpty()) {
            pClass = Integer.parseInt(toDivide[Constants.CLASS_INDEX]);
        }
    }

    public void setName() {
        if (toDivide[Constants.LAST_NAME_INDEX] != null && !toDivide[Constants.LAST_NAME_INDEX].isEmpty()) {
            if (toDivide[Constants.FIRST_NAME_INDEX] != null && !toDivide[Constants.FIRST_NAME_INDEX].isEmpty()) {
                name = toDivide[Constants.FIRST_NAME_INDEX] + " " + toDivide[Constants.LAST_NAME_INDEX];
            }
        } else {
            name = "";
        }
    }

    public void setSex() {
        if (toDivide[Constants.SEX_INDEX] != null && !toDivide[Constants.SEX_INDEX].isEmpty()) {
            sex = toDivide[Constants.SEX_INDEX];
        } else {
            sex = "";
        }
    }

    public void setAge() {
        if (toDivide[Constants.AGE_INDEX] != null && !toDivide[Constants.AGE_INDEX].isEmpty()) {
            age = Float.parseFloat(toDivide[Constants.AGE_INDEX]);
        } else {
            age = 0;
        }
    }

    public void setSibSp() {
        if (toDivide[Constants.SIBSIP_INDEX] != null && !toDivide[Constants.SIBSIP_INDEX].isEmpty()) {
            sibSp = Integer.parseInt(toDivide[Constants.SIBSIP_INDEX]);
        } else {
            sibSp = 0;
        }
    }

    public void setParch() {
        if (toDivide[Constants.PARCH_INDEX] != null && !toDivide[Constants.PARCH_INDEX].isEmpty()) {
            parch = Integer.parseInt(toDivide[Constants.PARCH_INDEX]);
        } else {
            parch = 0;
        }
    }

    public void setTicket() {
        if (toDivide[Constants.TICKET_INDEX] != null && !toDivide[Constants.TICKET_INDEX].isEmpty()) {
            ticket = toDivide[Constants.TICKET_INDEX];
        } else {
            ticket = "";
        }
    }

    public void setFare() {
        if (toDivide[Constants.FARE_INDEX] != null && !toDivide[Constants.FARE_INDEX].isEmpty()) {
            fare = Float.parseFloat(toDivide[Constants.FARE_INDEX]);
        } else {
            fare = 0;
        }
    }

    public void setCabin() {
        if (toDivide[Constants.CABIN_INDEX] != null && !toDivide[Constants.CABIN_INDEX].isEmpty()) {
            cabin = toDivide[Constants.CABIN_INDEX];
        } else {
            cabin = "";
        }
    }

    public void setEmbarked() {
        try {
            if (toDivide[Constants.EMBARKED_INDEX] != null && !toDivide[Constants.EMBARKED_INDEX].isEmpty()) {
                embarked = toDivide[Constants.EMBARKED_INDEX];
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
        String formattedName = toDivide[Constants.FIRST_NAME_INDEX] + " " + toDivide[Constants.LAST_NAME_INDEX];
        int dotIndex = formattedName.indexOf(".");
        formattedName = formattedName.substring(dotIndex + 1);
        formattedName = formattedName.replace("\"", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
        return formattedName;
    }

}
