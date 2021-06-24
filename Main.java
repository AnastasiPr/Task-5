package com.company;

import java.util.Scanner;
import java.util.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



public class Main {

    //1
    public static boolean SameLetterPattern(String str1, String str2) {
        return getPattern(str1).equals(getPattern(str2));
    }

    private static String getPattern(String str) {
        List<Character> strchar = new ArrayList<>();
        StringBuilder patternBuilder = new StringBuilder();

        for (char c: str.toCharArray()) {
            if (!strchar.contains(c)) {
                strchar.add(c);
                patternBuilder.append(strchar.size() - 1);
            } else {
                patternBuilder.append(strchar.indexOf(c));
            }
        }

        return patternBuilder.toString();
    }

    //2
    public static String SpiderVsFly(String spider, String fly){ //цена точки = цена до ближайшей + цена до мухи
        Point start = new Point(spider);
        Point end = new Point(fly);
        String way="";
        while (!start.getLetter().equals(end.getLetter()) || start.getChislo()!=end.getChislo()){

            way += start.getLetter() + String.valueOf(start.getChislo())+"-";
            start = whereToGo(start,end);

        }
        way += start.getLetter() + String.valueOf(start.getChislo());
        return way;
    }
    static class Point {
        private String letter;
        private int chislo;

        public Point(String p) {
            this.letter = String.valueOf(p.charAt(0)); //this требуется для того, чтобы метод мог сослаться на вызвавший его объект
            chislo = Integer.valueOf(String.valueOf(p.charAt(1)));

        }

        public Point(String letter, int chislo) {
            this.letter = letter;
            this.chislo = chislo;
        }

        public int getChislo() {
            return chislo;
        }

        public String getLetter() {
            return letter;
        }
    }

    public static double BetweenP(Point start, Point moveTo){
        if (start.getLetter().equals(moveTo.getLetter())) return 1;
        else{
            int a = start.getChislo();
            int b = moveTo.getChislo();
            return Math.sqrt(a*a + b*b - 2*a*b* Math.cos(Math.toRadians(45)));
        }

    }
    public static double calculateS(Point start, Point moveTo, Point fly){

        Map<String, Integer> web = new HashMap<String,Integer>();
        web.put("A",0); //для получения букв через цифры
        web.put("B",1);
        web.put("C",2);
        web.put("D",3);
        web.put("E",4);
        web.put("F",5);
        web.put("G",6);
        web.put("H",7);


        int angle, a,b;
        double distance;
        int difference = Math.abs(web.get(moveTo.getLetter())-web.get(fly.getLetter()));


        if (difference<=4)
            angle =  difference*45;
        else angle = (8 - difference)*45;
        a = moveTo.getChislo();
        b = fly.getChislo();
        if (difference!=0 && moveTo.getChislo()!=0)
            distance = Math.sqrt(a*a + b*b - 2*b*a*Math.cos(Math.toRadians(angle)));
        else distance=Math.abs(a-b);
        distance = distance+BetweenP(start,moveTo);
        return distance;
    }

    public static Point minimumDistance(Point spider,Point fly, Point moveTo1, Point moveTo2 ){
        if (calculateS(spider, moveTo1,fly)<calculateS(spider, moveTo2,fly)) return moveTo1;
        else return moveTo2;

    }

    public static Point whereToGo(Point spider, Point fly){
        Map<Integer, String> revWeb = new HashMap<Integer,String>();
        revWeb.put(0,"A");
        revWeb.put(1,"B");
        revWeb.put(2,"C");
        revWeb.put(3,"D");
        revWeb.put(4,"E");
        revWeb.put(5,"F");
        revWeb.put(6,"G");
        revWeb.put(7,"H");


        Map<String, Integer> web = new HashMap<String,Integer>();
        web.put("A",0);
        web.put("B",1);
        web.put("C",2);
        web.put("D",3);
        web.put("E",4);
        web.put("F",5);
        web.put("G",6);
        web.put("H",7);

        Point first;
        Point second;
        Point third;
        Point fourth;
        Point fin;


        String nextLetter;
        String pastLetter;


        if (spider.getChislo()==0) {
            return fin = new Point(fly.getLetter(),1);
        }

        if (!spider.getLetter().equals("H"))
            nextLetter = revWeb.get(web.get(spider.getLetter()) + 1);
        else
            nextLetter = "A";

        if (!spider.getLetter().equals("A"))
            pastLetter = revWeb.get(web.get(spider.getLetter()) - 1);
        else
            pastLetter = "H";


        third = new Point(nextLetter, spider.getChislo());
        fourth = new Point(pastLetter, spider.getChislo());
        fin = minimumDistance(spider,fly,third,fourth);


        if (spider.getChislo()<4 && spider.getChislo()>0) {
            first = new Point(spider.getLetter(), spider.getChislo() + 1);
            second = new Point(spider.getLetter(), spider.getChislo() - 1);
            fin = minimumDistance(spider,fly,fin, minimumDistance(spider,fly,first,second));
        }
        else if (spider.getChislo()==4){
            second = new Point(spider.getLetter(), spider.getChislo() - 1);
            fin = minimumDistance(spider,fly,fin,second);
        }


        return fin;

    }

    //3
    public static int DigitsCount(long n){
        int k=0;
        if (n!=0) {
            while (n!=0){
                n=n/10;
                k++;
            }
        } else return 1;
        return k;
    }

    //4
    public static int TotalPoints(String[] tries, String answer){
        char[] arrAnswer = answer.toCharArray();
        Map<Character, Integer> count = new HashMap<>();

        for (Character l:arrAnswer){ //заполнение мапы
            if (!count.containsKey(l)) count.put(l,1);
            else count.put(l,count.get(l)+1);
        }
        int sumOfPoints=0;
        for (String word: tries) {
            boolean doesItCount = true;
            int points=0;

            char[] arrTries = word.toCharArray();
            Map<Character, Integer> ansCount = new HashMap<>();

            for (Character l:arrTries){ //заполнение мапы
                if (!ansCount.containsKey(l)) ansCount.put(l,1);
                else ansCount.put(l,ansCount.get(l)+1);
            }

            for (Character l:arrTries) {
                if (!count.containsKey(l) || count.get(l)<ansCount.get(l)) //get получение значения по ключу
                    doesItCount=false;

            }
            if (doesItCount) {
                if (arrTries.length == arrAnswer.length) points += 54;
                else if (arrTries.length == 3) points += 1;
                else if (arrTries.length == 4) points += 2;
                else if (arrTries.length == 5) points += 3;
            }
            sumOfPoints+=points;

        }
        return sumOfPoints;
    }

    //5
    public static int LongestRun(int [] array) {
        int max = -1;
        int count = 1;
        for (int i = 0;i < array.length - 2;i++) {
            if (array[i+1] - array[i] == 1) count = count + 1;
            else count = 1;
            if (count > max) max = count;
        }
        return max;
    }

    //6
    public static int TakeDownAverage(int[] arr) {
        int sum = Arrays.stream(arr).sum(); //поток из массивов; укорачивает код
        double srzn = Math.round(sum/arr.length);
        return (int)((arr.length+1) * (srzn-5) - sum);
    }

    //7
    public static String Rearrange(String mixed){
        String[] words = mixed.split(" ");
        String out="";
        int position=1;
        if (words.length!=1)
            while (position<= words.length)
                for (String word:words) {
                    if (word.contains(String.valueOf(position))) { //valueOf - приводит к типу string
                        word = word.replace(String.valueOf(position), "");
                        out += word + " ";
                        position++;
                    }
                }
        return out;
    }

    //8
    public static String  MaxPossible(int a, int b){
        ArrayList<Integer> first = new ArrayList<>();
        ArrayList<Integer> second = new ArrayList<>();

        while (a>0){
            int t = a % 10;
            first.add(t);
            a = Math.floorDiv(a,10);
        }
        Collections.reverse(first);

        while (b>0){
            int t = b % 10;
            second.add(t);
            b = Math.floorDiv(b,10);
        }
        Collections.sort(second, Collections.reverseOrder());
        for (int i=0; i< first.size();i++){
            for (int j=0; j<second.size();j++) {
                if (first.get(i) < second.get(j)){
                    first.remove(i);
                    first.add(i,second.get(j)); //второе вместо первого
                    second.remove(j);
                    second.add(j,0);
                }
            }
        }
        String res = "";
        for (Integer t: first) {
            res += String.valueOf(t);
        }
        return res;
    }

    //9
    public static String TimeDifference(String cityA, String dateA, String cityB) {
        Map<String, String> timedef = new HashMap<>();
            timedef.put("Los Angeles", "-08:00");
            timedef.put("New York", "-05:00");
            timedef.put("Caracas", "-04:30");
            timedef.put("Buenos Aires", "-03:00");
            timedef.put("London", "+00:00");
            timedef.put("Rome", "+01:00");
            timedef.put("Moscow", "+03:00");
            timedef.put("Tehran", "+03:30");
            timedef.put("New Delhi", "+05:30");
            timedef.put("Beijing", "+08:00");
            timedef.put("Canberra", "+10:00");

        ZoneId cityAZone = ZoneId.ofOffset("", ZoneOffset.of(timedef.get(cityA)));
        ZoneId cityBZone = ZoneId.ofOffset("", ZoneOffset.of(timedef.get(cityB)));
        //Для хранения временной зоны в Java используется класс ZoneId
        //внутри получаем ZoneID, чтобы использовать его для внешнего
        //Чтобы получить объект ZoneId по его имени, нужно воспользоваться статическим методом of()
        //используемый для получения экземпляра ZoneId, переносящего смещение. Если префикс, переданный в этом методе, равен «GMT», «UTC» или «UT»,
        // то ZoneId с префиксом затем ненулевой смещение возвращается, и если префикс пуст «», то возвращается ZoneOffset.

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.US).withZone(cityAZone); //формат представления записи даты
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateA, formatter).withZoneSameInstant(cityBZone);
        //parse конвертирует дату в string
        //withZoneSameInstant () интерфейса ChronoZonedDateTime, используемый для возврата копии этого объекта ChronoZonedDateTime путем изменения часового пояса
        // и без мгновенного изменения.
        //Этот метод основан на сохранении того же самого момента времени, поэтому пробелы и
        // перекрытия в локальной временной шкале имеют не влияет на результат.
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-M-d HH:mm"));
    }

    //10
    public static boolean IsNew(int num) {
        List<Integer> numbers = new ArrayList<>();

        int numCopy = num;
        int numL = 0;
        while (numCopy > 0) {
            numL++;
            numbers.add((int) (numCopy % 10));
            numCopy /= 10;
        }
        numbers.sort(Collections.reverseOrder()); //по возрастанию

        int newNum = 0;
        int newNumL = 0;
        for (int i = 0; i < numbers.size(); i++) {
            int number = numbers.get(i);
            if (!(number == 0 && i == numbers.size() - 1)) newNumL++;
            newNum += number * (int) Math.pow(10, i);
        }

        return num == newNum || numL != newNumL;
    }


    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        //1
        System.out.println(SameLetterPattern(scan.nextLine(),scan.nextLine()));

        //2
        System.out.println(SpiderVsFly(scan.nextLine(),scan.nextLine()));

        //3
        System.out.println(DigitsCount(scan.nextLong()));

        //4
        System.out.println(TotalPoints(new String[]{"cat", "create", "sat"}, "caster"));
        System.out.println(TotalPoints(new String[]{"trance", "recant"}, "recant"));

        //5
        System.out.println(LongestRun(new int[]{1, 2, 3, 5, 6, 7, 8, 9 }));
        System.out.println(LongestRun(new int[]{1, 2, 3, 10, 11, 15 }));

        //6
        System.out.println(TakeDownAverage(new int[]{95,83,90,87,88,93}));
        System.out.println(TakeDownAverage(new int[]{10}));

        //7
        System.out.println(Rearrange("Tesh3 th5e 1I lov2e way6 she7 j4ust i8s."));
        System.out.println(Rearrange(" "));

        //8
        System.out.println(MaxPossible(scan.nextInt(),scan.nextInt()));

        //9
        System.out.println(TimeDifference("Los Angeles", "April 1, 2011 23:23", "Canberra"));
        System.out.println(TimeDifference("London", "July 31, 1983 23:01", "Rome"));

        //10
        System.out.println(IsNew(3));
        System.out.println(IsNew(30));
        System.out.println(IsNew(321));
    }
}
