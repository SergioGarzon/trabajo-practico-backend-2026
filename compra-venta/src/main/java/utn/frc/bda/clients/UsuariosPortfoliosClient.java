package utn.frc.bda.clients;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuariosPortfoliosClient {

    private final RestTemplate restTemplate;
    // URL base del otro microservicio (luego lo pasamos a application.properties)
    private final String USERSERVICEVENTA = "http://localhost:8081/api/ventas";
    private final String USERSERVICECOMPRA = "http://localhost:8081/api/billetera/operacion";

    public UsuariosPortfoliosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validarOrdenVenta(String simboloAccion, Long cantidad, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        // Pasamos el JWT al otro servicio para que no rebote por seguridad
        headers.set("Authorization", "Bearer " + jwtToken);

        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "simboloAccion", simboloAccion,
                "cantidadVender", cantidad
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Ejemplo de endpoint: /api/portfolios/validar?usuarioId=1&tipo=VENTA...
            String url = USERSERVICEVENTA + "/iniciar";
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            // Si el servicio rechaza (400/403) o está caído, devolvemos false
            return false;
        }
    }

    public boolean procesarVenta(Long idOrdenVenta, Double montoGanado, Long cantidadVendida ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = Map.of(
            "idOrdenVenta", idOrdenVenta,
            "dineroObtenido", montoGanado,
            "cantidadVendida", cantidadVendida
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        try {
            String url = USERSERVICEVENTA + "/procesar";
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        }catch (Exception e){
            return false;
        }
    }


    public boolean validarOrdenCompra(Long idOrden, Double cantidad, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "idOrdenCompra", idOrden,
                "monto", cantidad
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            String url = USERSERVICECOMPRA + "/bloquear";
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        }catch (Exception e) {
            return false;
        }
    }


    public boolean resolverOrdenCompra(Long idOrdenCompra, Long cantidadComprada, Double precioAcordado) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "idOrdenCompra", idOrdenCompra,
                "cantidadComprada", cantidadComprada,
                "precioAcordado", precioAcordado
        );
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            String url = USERSERVICECOMPRA + "/resolver";
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            return false;
        }
    }


}
