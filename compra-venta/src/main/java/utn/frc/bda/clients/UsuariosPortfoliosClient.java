package utn.frc.bda.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Component
public class UsuariosPortfoliosClient {

    private final RestTemplate restTemplate;
    // URL base del otro microservicio (luego lo pasamos a application.properties)
    private final String PORTFOLIO_SERVICE_URL = "http://localhost:8080/api/portfolios";

    public UsuariosPortfoliosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validarOperacion(Long usuarioId, String tipoOperacion, Double montoOCantidad, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        // Pasamos el JWT al otro servicio para que no rebote por seguridad
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Ejemplo de endpoint: /api/portfolios/validar?usuarioId=1&tipo=VENTA...
            String url = PORTFOLIO_SERVICE_URL + "/validar?usuarioId=" + usuarioId + "&tipo=" + tipoOperacion;
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class);

            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            // Si el servicio rechaza (400/403) o está caído, devolvemos false
            return false;
        }
    }
}