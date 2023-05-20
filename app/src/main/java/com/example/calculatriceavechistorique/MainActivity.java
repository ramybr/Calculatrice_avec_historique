package com.example.calculatriceavechistorique;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewOperation;
    private TextView textViewResult;
    private ListView listViewHistory;
    DatabaseHistorique dbh;

    private void loadOperationsFromDatabase() {
        dbh = new DatabaseHistorique(this);
        // Récupérer les opérations et résultats depuis la base de données en utilisant votre classe DatabaseHistorique
        List<String> operations = dbh.getAllOperations();

        // Créer un adaptateur pour afficher les opérations et résultats dans la liste
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, operations);

        // Définir l'adaptateur sur la liste
        listViewHistory.setAdapter(adapter);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewOperation = findViewById(R.id.textViewOperation);
        textViewResult = findViewById(R.id.textViewResult);

        // Boutons pour les chiffres de 0 à 9
        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonDecimal = findViewById(R.id.buttonDec);
        Button buttonPlus = findViewById(R.id.buttonPlus);
        Button buttonMultiply = findViewById(R.id.buttonMultiply);
        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonDivide = findViewById(R.id.buttonDivide);
        Button buttonEquals = findViewById(R.id.buttonEqual);
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonClear = findViewById(R.id.buttonClear);
        //Les textViews
         listViewHistory = findViewById(R.id.listViewHistory);
         textViewOperation = findViewById(R.id.textViewOperation);
         textViewResult = findViewById(R.id.textViewResult);




        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentOperation = textViewOperation.getText().toString();
                if (!currentOperation.isEmpty()) {
                    String newOperation = currentOperation.substring(0, currentOperation.length() - 1);
                    textViewOperation.setText(newOperation);
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewOperation.setText("");
                textViewResult.setText("");
            }
        });
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the TextView is not empty
                if (!TextUtils.isEmpty(textViewResult.getText())) {
                    // Clear the text to remove the operation and result
                    textViewOperation.setText("");
                    textViewResult.setText("");
                }

                // Récupérer le bouton cliqué
                Button button = (Button) v;

                // Récupérer le texte du bouton
                String buttonText = button.getText().toString();

                // Récupérer le texte actuel de la zone de texte "Opération"
                String currentOperation = textViewOperation.getText().toString();
                // Ajouter le chiffre ou l'opérateur au texte actuel
                String newOperation = currentOperation + buttonText;

                // Mettre à jour la zone de texte "Opération"
                textViewOperation.setText(newOperation);
            }
        };

        button0.setOnClickListener(numberClickListener);
        button1.setOnClickListener(numberClickListener);
        button2.setOnClickListener(numberClickListener);
        button3.setOnClickListener(numberClickListener);
        button4.setOnClickListener(numberClickListener);
        button5.setOnClickListener(numberClickListener);
        button6.setOnClickListener(numberClickListener);
        button7.setOnClickListener(numberClickListener);
        button8.setOnClickListener(numberClickListener);
        button9.setOnClickListener(numberClickListener);
        buttonMultiply.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        buttonDecimal.setOnClickListener(this);



        DatabaseHistorique dbh = new DatabaseHistorique(this);

        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentOperation = textViewOperation.getText().toString();
                // Calculer le résultat et l'afficher dans la zone de texte "textViewResult"
                BigDecimal result = calculateResult(currentOperation);
                textViewResult.setText(String.valueOf(result));
                String operation = textViewOperation.getText().toString();
                String resultat = textViewResult.getText().toString();

                dbh.insertOperation(operation, resultat);
                loadOperationsFromDatabase();
                listViewHistory.setSelection(listViewHistory.getCount() - 1);
            }
        });


    }

    @Override
    public void onClick(View v) {
        // Récupérer le bouton cliqué
        Button button = (Button) v;

        // Récupérer le texte du bouton
        String buttonText = button.getText().toString();

        // Récupérer le texte actuel de la zone de texte "Opération"
        String currentOperation = textViewOperation.getText().toString();

        // Ajouter le chiffre ou l'opérateur au texte actuel
        String newOperation = currentOperation + buttonText;

        // Mettre à jour la zone de texte "Opération"
        textViewOperation.setText(newOperation);
        }


    private BigDecimal calculateResult(String operation) {
        // Supprimer les espaces de la chaîne d'opération
        operation = operation.replaceAll(" ", "");

        BigDecimal result = null;
        BigDecimal operand = null;
        String operator = null;

        // Parcourir la chaîne jusqu'à trouver un opérateur
        for (int i = 0; i < operation.length(); i++) {
            char ch = operation.charAt(i);


            if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                // Récupérer l'opérateur et les opérandes
                operator = String.valueOf(ch);
                BigDecimal firstOperand = new BigDecimal(operation.substring(0, i));
                BigDecimal secondOperand = new BigDecimal(operation.substring(i + 1));

                // Effectuer l'opération en fonction de l'opérateur
                switch (operator) {
                    case "+":
                        result = firstOperand.add(secondOperand);
                        break;
                    case "-":
                        result = firstOperand.subtract(secondOperand);
                        break;
                    case "*":
                        result = firstOperand.multiply(secondOperand);
                        break;
                    case "/":
                        result = firstOperand.divide(secondOperand, 20, RoundingMode.HALF_UP).stripTrailingZeros();
                        if (result.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                            int integerValue = result.intValue();
                            result = BigDecimal.valueOf(integerValue); // Assign as integer value
                        }
                        break;
                }

                // Sortir de la boucle une fois l'opération effectuée
                break;
            }
        }

        return result;
    }


}
