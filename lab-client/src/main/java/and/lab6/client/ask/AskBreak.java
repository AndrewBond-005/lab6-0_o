package and.lab6.client.ask;

public class AskBreak extends Exception {
    private final String report;

    public AskBreak(String report) {
        this.report = report;
    }
    public String getReport() {
        return report;
    }
    //    public static class AskBreak {
//    }

//
//    public static Worker askWorker(Console console, int id) throws AskBreak {
//        try {
//            String name;
//            while (true) {
//                console.print("Введите имя name: ");
//                name = console.readln().trim();
//                if (name.equals("exit")) throw new AskBreak();
//                if (name.equals("this") && UpdateID.worker != null) {
//                    name = UpdateID.worker.getName();
//                }
//                if (!name.isEmpty()) break;
//                else
//                    console.print("Имя не может быть пустой строкой!");
//            }
//            Coordinates coordinates = askCoordinates(console);
//
//            var creationDate = LocalDateTime.now();
//            Integer salary;
//            while (true) {
//                console.print("Введите зарплату salary: ");
//                var line = console.readln().trim();
//                if (line.equals("exit")) throw new AskBreak();
//                if (line.isEmpty()) {
//                    salary = null;
//                    break;
//                }
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                        salary = UpdateID.worker.getSalary();
//                    } else {
//                        salary = Integer.parseInt(line);
//                    }
//
//                    if (salary > 0) break;
//                    else console.print(" Введено неположительное число!");
//                } catch (NumberFormatException e) {
//                    console.print("Ошибка! зарпалата - целое положительное число!");
//                }
//            }
//            LocalDate endDate = askDate(console);
//            Position position = askEnum(Position.class, console);
//            Status status = askEnum(Status.class, console);;
//            Person person = askPerson(console);
//            return new Worker(id, name, coordinates, creationDate, salary, endDate, position, status, person);
//        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Ошибка чтения!!!");
//            return null;
//        }
//    }
//
//    public static Person askPerson(Console console) throws AskBreak {
//        try {
//            Double weight;
//            while (true) {
//                console.print("Введите вес weight: ");
//                var line = console.readln().trim();
//                if (line.equals("exit")) throw new AskBreak();
//                if (line.isEmpty()) {
//                    weight = null;
//                    break;
//                }
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null && UpdateID.worker.getPerson() != null) {
//                        weight = UpdateID.worker.getPerson().getWeight();
//                    } else {
//                        weight = Double.parseDouble(line);
//                    }
//                    if (weight > 0) break;
//                    else
//                        console.println("Ошибка! Введено отрицательное число!");
//                } catch (NumberFormatException e) {
//                    console.println("Ошибка! Вес -  положительное число!");
//                }
//            }
//
//            EyeColor eyeColor = askEnum(EyeColor.class, console);
//            HairColor hairColor = askEnum(HairColor.class, console);
//            Country nationality = askEnum(Country.class, console);
//
//            return new Person(weight, eyeColor, hairColor, nationality);
//        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Ошибка чтения");
//            return null;
//        }
//    }
//
//    public static <T extends Enum<T>> T askEnum(Class<T> param, Console console) throws AskBreak {
//        while (true) {
//            console.print("Введите "+"значение"+" "+ param.getSimpleName() + Arrays.toString(param.getEnumConstants()) + ": ");
//            var line = console.readln().trim();
//            if (line.equals("exit")) throw new AskBreak();
//            if (!line.isEmpty()) {
//                if (line.equalsIgnoreCase("this") && UpdateID.worker != null && UpdateID.worker.getPerson() != null) {
//                    try {
//                        Method method = Worker.class.getMethod("get" + param.getSimpleName());
//                        return (T) method.invoke(UpdateID.worker);
//                    } catch (NoSuchMethodException e) {
//                        try {
//                            Method method = Person.class.getMethod("get" + param.getSimpleName());
//                            return (T) method.invoke(UpdateID.worker.getPerson());
//                        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
//                            console.println("Ошибка! Что-то пошло не так");
//                        }
//                    } catch (InvocationTargetException|IllegalAccessException e) {
//                        console.println("Ошибка! Что-то пошло не так");
//                    }
//                }
//                else try {
//                    var res= T.valueOf(param, line.toUpperCase());
//                    return res;
//                } catch (IllegalArgumentException e) {
//                    console.println("Ошибка! Введённае значение "+line.toUpperCase()+" не найдено среди значений "+param.getSimpleName());
//                    if(console.getFileScanner()!=null){
//                        return null;
//                    }
//                }
//            } else return null;
//        }
//    }
//
//    public static Coordinates askCoordinates(Console console) throws AskBreak {
//        try {
//            float x;
//            while (true) {
//                console.print("Введите первую координату coordinates.x: ");
//                var line = console.readln().trim();
//                if (line.equals("exit")) throw new AskBreak();
//                if (!line.isEmpty()) {
//                    try {
//                        if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                            x = UpdateID.worker.getCoordinates().getX();
//                        } else {
//                            x = Float.parseFloat(line);
//                        }
//                        break;
//
//                    } catch (NumberFormatException e) {
//                        console.print("Ошибка! Введено не число!");
//                    }
//                }
//            }
//            Long y;
//            while (true) {
//                console.print("Введите вторую координату coordinates.y: ");
//                var line = console.readln().trim();
//                if (line.equals("exit")) throw new AskBreak();
//                if (!line.isEmpty()) {
//                    try {
//                        if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                            y = UpdateID.worker.getCoordinates().getY();
//                        } else {
//                            y = Long.parseLong(line);
//                        }
//                        break;
//                    } catch (NumberFormatException e) {
//                        console.print("Ошибка! Введено не целое число!");
//
//                    }
//                }
//            }
//            return new Coordinates(x, y);
//        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Ошибка чтения");
//            return null;
//        }
//    }
//
//    public static LocalDate askDate(Console console) throws AskBreak {
//        try {
//            LocalDate endDate;
//            while (true) {
//                console.print("Введите значение даты окончания работы EndDate (Exemple: " +
//                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + " or 2025-05-25): ");
//                var line = console.readln().trim();
//                if (line.equals("exit")) throw new AskBreak();
//                if (line.isEmpty()) {
//                    endDate = null;
//                    break;
//                }
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                        endDate = UpdateID.worker.getEndDate();
//                    } else {
//                        endDate = LocalDate.parse(line, DateTimeFormatter.ISO_DATE);
//                    }
//                    break;
//                } catch (DateTimeParseException e) {
//                    console.println("Ошибка! Введённая строка не соответсвует формату даты");
//
//                }
//            }
//            return endDate;
//        } catch (NoSuchElementException | IllegalStateException e) {
//            console.printError("Ошибка чтения");
//            return null;
//        }
//    }
}
//public static HairColor askHairColor(Console console) throws AskBreak {
//    try {
//        HairColor hairColor;
//        while (true) {
//            console.print("Введите цвет волос HairColor(" + Arrays.toString(HairColor.values()) + "): ");
//            var line = console.readln().trim();
//            if (line.equals("exit")) throw new AskBreak();
//            if (!line.isEmpty()) {
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null && UpdateID.worker.getPerson().getHairColor() != null) {
//                        hairColor = UpdateID.worker.getPerson().getHairColor();
//                    } else {
//                        hairColor = HairColor.valueOf(line);
//                    }
//                    break;
//                } catch (NullPointerException | IllegalArgumentException e) {
//                    console.println("Ошибка! Введённая строка не соответсвует ни одному из возможных занчений цвета волос");
//
//                }
//            } else return null;
//        }
//        return hairColor;
//    } catch (NoSuchElementException | IllegalStateException e) {
//        console.printError("Ошибка чтения");
//        return null;
//    } catch (AskBreak e) {
//        throw new RuntimeException(e);
//    }
//}
//
//public static Country askCountry(Console console) throws AskBreak {
//    try {
//        Country nationality;
//        while (true) {
//            console.println("Введите страну Country(" + Arrays.toString(Country.values()) + "): ");
//            var line = console.readln().trim();
//            if (line.equals("exit")) throw new AskBreak();
//            if (!line.isEmpty()) {
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null && UpdateID.worker.getPerson().getNationality() != null) {
//                        nationality = UpdateID.worker.getPerson().getNationality();
//                    } else {
//                        nationality = Country.valueOf(line);
//                    }
//                    break;
//                } catch (NullPointerException | IllegalArgumentException e) {
//                    console.println("Ошибка! Введённая строка не соответсвует ни одному из возможных занчений стран");
//
//                }
//            } else return null;
//        }
//        return nationality;
//    } catch (NoSuchElementException | IllegalStateException e) {
//        console.printError("Ошибка чтения");
//        return null;
//    } catch (AskBreak e) {
//        throw new RuntimeException(e);
//    }
//}
//
//private static Position askPosition(Console console) throws AskBreak {
//    try {
//        Position position;
//        while (true) {
//            console.print("Введите должность Position(" + Arrays.toString(Position.values()) + "):");
//            var line = console.readln().trim();
//            if (line.equals("exit")) throw new AskBreak();
//            if (!line.isEmpty()) {
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                        position = UpdateID.worker.getPosition();
//                    } else {
//                        position = Position.valueOf(line);
//                    }
//                    break;
//                } catch (NullPointerException | IllegalArgumentException e) {
//                    console.println("Ошибка! Введённая строка не соответсвует ни одному из возможных занчений должности");
//
//                }
//            } else return null;
//        }
//        return position;
//    } catch (NoSuchElementException | IllegalStateException e) {
//        console.printError("Ошибка чтения");
//        return null;
//    } catch (AskBreak e) {
//        throw new RuntimeException(e);
//    }
//}
//
//public static Status askStatus(Console console) throws AskBreak {
//    try {
//        Status status;
//        while (true) {
//            console.print("Введите статус Status(" + Arrays.toString(Status.values()) + "): ");
//            var line = console.readln().trim();
//            if (line.equals("exit")) throw new AskBreak();
//            if (!line.isEmpty()) {
//                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                        status = UpdateID.worker.getStatus();
//                    } else {
//                        status = Status.valueOf(line);
//                    }
//                    break;
//                } catch (NullPointerException | IllegalArgumentException e) {
//                    console.print("Ошибка!  Введённая строка не соответсвует ни одному из возможных занчений статуса");
//                }
//            } else return null;
//        }
//        return status;
//    } catch (NoSuchElementException | IllegalStateException e) {
//        console.printError("Ошибка чтения");
//        return null;
//    } catch (AskBreak e) {
//        throw new RuntimeException(e);
//    }
//}