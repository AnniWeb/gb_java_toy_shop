import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Shop
 */
public class Shop {
    // Кодировка по умл.
    private static String cp = System.getProperty("console.encoding","Cp866");

    private static Set<Raffle> raffles;
    private static Map<Date, Raffle> results;

    public static void main(String[] args) {
        raffles = getDefaulrRaffles();
        results = getDefaulrResult();

        try {
            // чтение данных с указанием кодировки, для решения проблем с кириллицей
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, cp));
            while (true) {
                System.out.println("");
                printMenu();

                try {
                    int choice = Integer.parseInt(reader.readLine());
                    execChoice(reader, choice);
                } catch (ToyShopException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<Raffle> getDefaulrRaffles()
    {
        Set<Raffle> raffles = new HashSet<>();
        raffles.add(new Raffle(1, "Мишка", 10, 30));
        raffles.add(new Raffle(2, "Кукла", 10, 35));
        raffles.add(new Raffle(3, "Мячик", 10, 1));
        raffles.add(new Raffle(4, "Заяц", 10, 40));

        return raffles;
    }

    private static Raffle getRaffleByToyId(int toyId)
    {
        for (Raffle raffle : raffles) {
            if (raffle.getToyId() == toyId) {
                return raffle;
            }
        }
        return null;
    }

    private static Map<Date, Raffle> getDefaulrResult()
    {
        Map<Date, Raffle> results = new HashMap<>();

        return results;
    }

    private static Raffle getRandomToy()
    {
        int totalWeight = 0;
        for (Raffle raffle : raffles) {
            totalWeight += raffle.getRate() * raffle.getQuantity();
        }

        Random random = new Random();
        int randomWeight = random.nextInt(totalWeight) + 1; // +1 чтобы избежать нуля

        int cumulativeWeight = 0;
        for (Raffle raffle : raffles) {
            cumulativeWeight += raffle.getRate() * raffle.getQuantity();
            if (randomWeight <= cumulativeWeight) {
                return raffle;
            }
        }
        return null;
    }

    private static void addRaffle(int toyId, String title, int quantity, int rate) throws ToyShopException
    {
        if (getRaffleByToyId(toyId) != null) {
            throw new ToyShopException("Игрушка уже участвует в розыгрыше");
        }
        raffles.add(new Raffle(toyId, title, quantity, rate));
    }

    private static void editRaffle(Raffle raffle, String title, int quantity, int rate)
    {
        if (title.length() > 0) {
            raffle.setTitle(title);
            System.out.println("Описание игрушки обновлено");
        }
        if (quantity > 0) {
            raffle.setQuantity(quantity);
            System.out.println("Розыгрываемое количество обновлено");
        }
        if (rate > 0) {
            raffle.setRate(rate);
            System.out.println("Частота выпадения обновлена");
        }
        return;
    }

    private static void addResult(Raffle raffle) throws ToyShopException
    {
        if (raffle.getQuantity() <= 0)
        {
            throw new ToyShopException("Игрушка закончилась");
        }
        Date date = new Date(System.currentTimeMillis()); 

        results.put(date, raffle);
        raffle.setQuantity(raffle.getQuantity() - 1);
    }

    private static void printRaffle(Raffle raffle)
    {
        StringBuilder str = new StringBuilder();
        str.append("Ид игрушки: ");
        str.append(raffle.getToyId());
        str.append("; Описание игрушки: ");
        str.append(raffle.getTitle());
        str.append("; Разыгрываемое кол-во: ");
        str.append(raffle.getQuantity());
        str.append("; Частота выпадения: ");
        str.append(raffle.getRate());
        System.out.println(str);
    }

    private static void printWinRaffle(Raffle raffle)
    {
        StringBuilder str = new StringBuilder();
        str.append("Ид игрушки: ");
        str.append(raffle.getToyId());
        str.append("; Описание игрушки: ");
        str.append(raffle.getTitle());
        System.out.println(str);
    }

    private static void showRaffles()
    {
        if (raffles.isEmpty()) {
            System.out.println("Розыгрыш завершен\n");
        }
        for (Raffle raffle : raffles) {
            printRaffle(raffle);
        }
        System.out.println("");
    }

    private static void showResults()
    {
        if (results.isEmpty()) {
            System.out.println("Розыгрыш еще не проводился\n");
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); 
        for (Map.Entry<Date, Raffle> entry : results.entrySet()) {
            StringBuilder str = new StringBuilder();
            str.append("Дата розыгрыша: ");
            str.append(formatter.format(entry.getKey()));
            str.append("; Ид игрушки: ");
            str.append(entry.getValue().getToyId());
            str.append("; Описание игрушки: ");
            str.append(entry.getValue().getTitle());
            System.out.println(str);
        }
        System.out.println("");
    }

    
    private static void printMenu()
    {
        System.out.println("Введите цифру, соответствующую необходимому критерию:");
        System.out.println("1 - Добавить игрушку");
        System.out.println("2 - Изменить параметры розыгрыша");
        System.out.println("3 - Список розыгрыша");
        System.out.println("4 - Провести розыгрыш");
        System.out.println("5 - Список выигранных призов");
        System.out.println("0 - Выход");
    }

    private static int parseInt(String field, String value, boolean req, int defaultValue) throws ToyShopException
    {
        if (value == "" && req) {
            throw new ToyShopException(field + " обязательно для заполнения");
        }
        return value == "" ? defaultValue : Integer.parseInt(value);
    }

    private static void execChoice(BufferedReader reader, int choice) throws ToyShopException, NumberFormatException, IOException
    {
        switch (choice) {
            case 0:
                System.out.println("До свидания!");
                System.exit(0);
                break;
            case 1:
                {
                    System.out.print("Введите ид игрушки: ");
                    int toyId = parseInt("Ид игрушки", reader.readLine(), true, 0);
                    System.out.print("Введите описание игрушки: ");
                    String title = reader.readLine();
                    if (title == "") {
                        throw new ToyShopException("Описание обязательно для заполнения");
                    }
                    System.out.print("Введите розыгрываемое количество: ");
                    int quantity = parseInt("Количество", reader.readLine(), true, 0);
                    System.out.print("Введите частоту выпадения в %: ");
                    int rate = parseInt("", reader.readLine(), true, 0); 

                    addRaffle(toyId, title, quantity, rate);
                }
                break;
            case 2:
                {
                    System.out.print("Введите ид игрушки: ");
                    int toyId = parseInt("Ид игрушки", reader.readLine(), false, 0);

                    Raffle raffle = getRaffleByToyId(toyId);
                    if (raffle == null) {
                        throw new ToyShopException("Игрушка не найдена");
                    }

                    System.out.print("Введите описание игрушки: ");
                    String title = reader.readLine();
                    System.out.print("Введите розыгрываемое количество: ");
                    int quantity = parseInt("Количество", reader.readLine(), false, 0);
                    System.out.print("Введите частоту выпадения в %: ");
                    int rate = parseInt("Частота выпадения", reader.readLine(), false, 0);
                    
                    editRaffle(raffle, title, quantity, rate);
                }  
                break;
            case 3:
                showRaffles();
                break;
            case 4:
                Raffle prize = getRandomToy();
                if (prize == null) {
                    System.out.println("Неудачный розыгрыш.");
                } else {
                    addResult(prize);
                    System.out.println("Приз:");
                    printWinRaffle(prize);
                }
                break;
            case 5:
                showResults();
                break;
            default:
                System.out.println("Некорректный выбор.");
                return;
        }
    }
}