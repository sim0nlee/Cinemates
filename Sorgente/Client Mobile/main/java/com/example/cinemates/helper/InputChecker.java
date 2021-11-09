package com.example.cinemates.helper;

import android.util.Patterns;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

public class InputChecker {

    public static boolean isEmail (EditText et, String email) {
        if (email.isEmpty()) {
            et.setError("Inserisci l'Email");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            et.setError("Email non valida");
            return false;
        }
        return true;
    }

    public static boolean isName (EditText et, String name) {
        if (!name.trim().matches("([a-zA-Z\\s]+){2,}")) {
            et.setError("Il nome e il cognome possono contenere solo lettere e spazi e devono essere lunghi almeno due caratteri");
            return false;
        }
        return true;
    }

    public static boolean isPassword (EditText et, String password) {
        if (password.isEmpty()) {
            et.setError("Inserisci la password");
            return false;
        }
        if (password.length() < 6) {
            et.setError("La password deve essere lunga almeno 6 caratteri");
            return false;
        }
        if (password.startsWith(" ") || password.endsWith(" ")) {
            et.setError("La password non può iniziare o finire con uno spazio");
            return false;
        }
        return true;
    }

    public static String capitalizeNames (String s) {

        final List<String> particles = Arrays.asList("d'", "de", "di", "del", "du", "von", "of");

        String[] tokens = s.split(" ");
        StringBuilder newName = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            if (particles.contains(tokens[i]) && i < tokens.length - 1) {
                newName.append(tokens[i]).append(" ");
            } else {
                newName.append(tokens[i].length() > 0 ? tokens[i].substring(0, 1).toUpperCase() + tokens[i].substring(1) + " " : "");
            }
        }

        return newName.toString().trim();

    }

}
