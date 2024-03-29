package musicStreaming._global.externalSystemProxy;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import musicStreaming._global.externalSystemProxy.reqDtos.EchoWithJsonReqDto;
import musicStreaming._global.externalSystemProxy.reqDtos.ExternalSystemProxyReqDto;
import musicStreaming._global.externalSystemProxy.reqDtos.GetDataUrlReqDto;
import musicStreaming._global.externalSystemProxy.resDtos.EchoWithJsonResDto;
import musicStreaming._global.externalSystemProxy.resDtos.ExternalSystemProxyResDto;
import musicStreaming._global.externalSystemProxy.resDtos.GetDataUrlResDto;
import musicStreaming._global.logger.CustomLogger;
import musicStreaming._global.logger.CustomLoggerType;

@Service
public class ExternalSystemProxyService {

    @Value("${externalSystem.ip}")
    private String externalSystemIp;

    @Value("${externalSystem.port}")
    private String externalSystemPort;

    
    // JSON 송수신 여부를 간편하게 테스트해보기 위해서
    public EchoWithJsonResDto echoWithJson(EchoWithJsonReqDto echoWithJsonReqDto) throws Exception {
        return this.jsonCommunication("/sanityCheck/echoWithJson", echoWithJsonReqDto, EchoWithJsonResDto.class);
    }

    // 주어진 DataUrlCode에 해당하는 DataUrl을 반환하기 위해서
    public GetDataUrlResDto getDataUrl(GetDataUrlReqDto getDataUrlReqDto) throws Exception {
        return this.jsonCommunication("/musics/getDataUrl", getDataUrlReqDto, GetDataUrlResDto.class);
    }


    // ExternalSystem과 JSON을 기반으로 한 일관성 있는 통신을 위해서
    public <S extends ExternalSystemProxyReqDto, R extends ExternalSystemProxyResDto> R jsonCommunication(String requestPath, S reqDto, Class<R> resType) throws Exception {
        try {

            String requestUrl = String.format("http://%s:%s", this.externalSystemIp, this.externalSystemPort) + requestPath;
            CustomLogger.debug(CustomLoggerType.EFFECT, "Request to external system",String.format("{requestUrl: %s, reqDto: %s}", requestUrl, reqDto));

            String resultRawText = WebClient.create(requestUrl)
                .put()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reqDto.hashMap()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

            if(resultRawText.length() <= 100)
                CustomLogger.debug(CustomLoggerType.EFFECT, "Read results from external system", String.format("{resultRawText: %s}", resultRawText));
            else
                CustomLogger.debug(CustomLoggerType.EFFECT, "Read results from external system", String.format("{resultRawTextLength: %s}", resultRawText.length()));

            ObjectMapper mapper = new ObjectMapper();
            R resDto = mapper.readValue(resultRawText, resType);
            return resDto;

        } catch (Exception e) {
            CustomLogger.error(e, "Error while requesting to externalSystem", String.format("{requestPath: %s, reqDto: %s}", requestPath, reqDto));
            throw e;
        }
    }
}
