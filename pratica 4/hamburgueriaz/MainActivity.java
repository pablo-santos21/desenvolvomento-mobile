package com.example.hamburgueriaz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int qtd = 0;
    private final int basePrice = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText nomeCliente = findViewById(R.id.name);
        CheckBox baconCheckbox = findViewById(R.id.bacon);
        CheckBox queijoCheckbox = findViewById(R.id.queijo);
        CheckBox onionCheckbox = findViewById(R.id.onion);
        TextView quantidadeView = findViewById(R.id.qtdProd);
        TextView resumoPedido = findViewById(R.id.price);

        Button botaoSomar = findViewById(R.id.buttonMore);
        Button botaoSubtrair = findViewById(R.id.buttonLess);
        Button botaoEnviar = findViewById(R.id.payment);

        botaoSomar.setOnClickListener(v -> {
            qtd++;
            atualizarQuantidade(quantidadeView);
            resumoPedido(resumoPedido, baconCheckbox.isChecked(), queijoCheckbox.isChecked(), onionCheckbox.isChecked());
        });


        botaoSubtrair.setOnClickListener(v -> {
            if (qtd > 0) {
                qtd--;
                atualizarQuantidade(quantidadeView);
                resumoPedido(resumoPedido, baconCheckbox.isChecked(), queijoCheckbox.isChecked(), onionCheckbox.isChecked());
            } else {
                Toast.makeText(this, "A quantidade não pode ser negativa!", Toast.LENGTH_SHORT).show();
            }
        });


        botaoEnviar.setOnClickListener(v -> {
            String nome = nomeCliente.getText().toString();
            boolean temBacon = baconCheckbox.isChecked();
            boolean temQueijo = queijoCheckbox.isChecked();
            boolean temOnion = onionCheckbox.isChecked();

            int precoFinal = calcularPrecoFinal(temBacon, temQueijo, temOnion);
            String resumo = gerarResumoPedido(nome, temBacon, temQueijo, temOnion, precoFinal);

            enviarPedido(nome, resumo);;
        });

        baconCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                resumoPedido(resumoPedido, baconCheckbox.isChecked(), queijoCheckbox.isChecked(), onionCheckbox.isChecked())
        );

        queijoCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                resumoPedido(resumoPedido, baconCheckbox.isChecked(), queijoCheckbox.isChecked(), onionCheckbox.isChecked())
        );

        onionCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                resumoPedido(resumoPedido, baconCheckbox.isChecked(), queijoCheckbox.isChecked(), onionCheckbox.isChecked())
        );
    }


    private void atualizarQuantidade(TextView quantidadeView) {
        quantidadeView.setText(String.valueOf(qtd));
    }

    private void resumoPedido(TextView resumoPedido, boolean temBacon, boolean temQueijo, boolean temOnion) {
        int precoAdicionais = 0;

        if (temBacon) precoAdicionais += 2;
        if (temQueijo) precoAdicionais += 2;
        if (temOnion) precoAdicionais += 3;


        int precoTotal = (basePrice + precoAdicionais) * qtd;
        resumoPedido.setText("Preço atual: R$ " + precoTotal);

    }


    private int calcularPrecoFinal(boolean temBacon, boolean temQueijo, boolean temOnion) {
        int precoAdicionais = 0;

        if (temBacon) precoAdicionais += 2;
        if (temQueijo) precoAdicionais += 2;
        if (temOnion) precoAdicionais += 3;

        return qtd * (basePrice + precoAdicionais);
    }


    private String gerarResumoPedido(String nome, boolean temBacon, boolean temQueijo, boolean temOnion, int precoFinal) {
        return "Nome do cliente: " + nome + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnion ? "Sim" : "Não") + "\n" +
                "Quantidade: " + qtd + "\n" +
                "Preço final: R$ " + precoFinal;
    }

    private void enviarPedido(String nomeCliente, String resumoPedido) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //intent.setData(android.net.Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"binho.bgu@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nomeCliente);
        intent.putExtra(Intent.EXTRA_TEXT, resumoPedido);

        if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivity(intent);
            startActivity(Intent.createChooser(intent, "Escolha um aplicativo de e-mail:"));
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

}