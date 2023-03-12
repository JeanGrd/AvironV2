public class Main {
    public static void main(String[] args) {
        Tournoi test = new Tournoi("test","Narbonne", "FM4X+", 2, 5,TypeTournoi.COURSE_LIGNE);
        System.out.println(test.getNb_participants());
    }
}