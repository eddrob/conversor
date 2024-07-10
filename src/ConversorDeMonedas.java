import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConversorDeMonedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Bienvenido al conversor de monedas");
            System.out.println("1. Convertir USD a ARS");
            System.out.println("2. Convertir ARS a USD");
            System.out.println("3. Convertir BRL a USD");
            System.out.println("4. Convertir USD a BRL");
            System.out.println("5. Convertir EUR a USD");
            System.out.println("6. Convertir USD a EUR");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            if (opcion == 7) {
                System.out.println("Finalizando la aplicación...");
                break;
            }

            System.out.print("Ingrese el valor a convertir: ");
            double valor = scanner.nextDouble();

            try {
                double resultado = realizarConversion(opcion, valor);
                System.out.printf("El valor convertido es: %.2f%n", resultado);
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static double realizarConversion(int opcion, double valor) throws Exception {
        String fromCurrency = "";
        String toCurrency = "";
        switch (opcion) {
            case 1:
                fromCurrency = "USD";
                toCurrency = "ARS";
                break;
            case 2:
                fromCurrency = "ARS";
                toCurrency = "USD";
                break;
            case 3:
                fromCurrency = "BRL";
                toCurrency = "USD";
                break;
            case 4:
                fromCurrency = "USD";
                toCurrency = "BRL";
                break;
            case 5:
                fromCurrency = "EUR";
                toCurrency = "USD";
                break;
            case 6:
                fromCurrency = "USD";
                toCurrency = "EUR";
                break;
            default:
                throw new IllegalArgumentException("Opción no válida");
        }

        return obtenerTasaDeCambio(fromCurrency, toCurrency) * valor;
    }

    private static double obtenerTasaDeCambio(String fromCurrency, String toCurrency) throws Exception {
        String apiKey = "527fdb785d11ed9c8817ed8b"; // Tu clave de API
        String url = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", apiKey, fromCurrency);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en la consulta de la API");
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
        return jsonObject.getAsJsonObject("conversion_rates").get(toCurrency).getAsDouble();
    }
}
