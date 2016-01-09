package net.anmlmc.ImperiumCore.Utils;

/**
 * Created by Anml on 1/9/16.
 */
public class Utils {

    public long longLength(String length) {
        length = actualLength(length);
        long amount = 0;
        String temp = "";

        for (char c : length.toLowerCase().toCharArray()) {
            if (Character.isDigit(c)) {
                temp += c;
            } else {
                if (temp.length() != 0) {
                    switch (c) {
                        case 'd':
                            amount += (Integer.parseInt(temp) * 86400000);
                            break;
                        case 'h':
                            amount += (Integer.parseInt(temp) * 3600000);
                            break;
                        case 'm':
                            amount += (Integer.parseInt(temp) * 60000);
                            break;
                        case 's':
                            amount += (Integer.parseInt(temp) * 1000);
                            break;
                    }
                    temp = "";
                }
            }
        }

        return amount;
    }

    public String actualLength(String length) {
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        String temp = "";

        for (char c : length.toLowerCase().toCharArray()) {
            if (Character.isDigit(c)) {
                temp += c;
            } else {
                if (temp.length() != 0) {
                    switch (c) {
                        case 'd':
                            days += (Integer.parseInt(temp));
                            break;
                        case 'h':
                            hours += (Integer.parseInt(temp));
                            break;
                        case 'm':
                            minutes += (Integer.parseInt(temp));
                            break;
                        case 's':
                            seconds += (Integer.parseInt(temp));
                            break;
                    }
                    temp = "";
                }
            }
        }

        temp = "";
        temp += days != 0 ? days + "d" : "";
        temp += hours != 0 ? hours + "h" : "";
        temp += minutes != 0 ? minutes + "m" : "";
        temp += seconds != 0 ? seconds + "s" : "";

        return temp;
    }
}
