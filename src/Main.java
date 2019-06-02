import com.oocourse.uml1.interact.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner zsarun = AppRunner.newInstance(MyUmlInteraction.class);
        zsarun.run(args);
    }
}
