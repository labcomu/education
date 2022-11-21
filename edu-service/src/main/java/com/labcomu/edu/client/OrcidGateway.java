package com.labcomu.edu.client;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.labcomu.edu.EduService;
import com.labcomu.edu.configuration.EduProperties;
import com.labcomu.edu.resource.Researcher;

@Component
@Validated
public class OrcidGateway {
	
	private Logger LOG = LoggerFactory.getLogger(OrcidGateway.class);
	
    private final String fetchResearcherUrl;
    private final WebClient.Builder webClientBuilder;
    
    
    public OrcidGateway(final WebClient.Builder webClientBuilder,
            final EduProperties properties) {
        this.webClientBuilder = webClientBuilder;
        this.fetchResearcherUrl = properties.getUrl().getFetchResearcherDetails();

    }

    public Researcher getResearcher(@NotNull final String orcid, int i) {
    	try {
	        return webClientBuilder.build()
	                .get()
	                .uri(fetchResearcherUrl, orcid)
	                .accept(MediaType.APPLICATION_JSON)
	                .retrieve()
	                .bodyToMono(Researcher.class)
	                .block();
    	} catch (WebClientResponseException e) {
    		
    		if(i == 3) {
    			LOG.error("Limite de tentativas de conexão com ORCID Service atingido");
    			LOG.warn("Retornando Researcher vazio");
    			
    			return new Researcher();
    		}
    		
    		LOG.warn(String.format("%dª tentativa de conexão com ORCID Service falhou. Tentando novamente...", i + 1));
			return getResearcher(orcid, i + 1);
		}
    }
}
