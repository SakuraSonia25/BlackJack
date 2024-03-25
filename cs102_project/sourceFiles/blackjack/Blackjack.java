package blackjack;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import blackjack.menus.Menu;

public class Blackjack {

    public static PrintWriter stdout = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8),true);
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.MainOptions();
    }
}
