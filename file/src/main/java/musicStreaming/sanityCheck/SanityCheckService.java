package musicStreaming.sanityCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import musicStreaming._global.event.MusicFileDeleteRequested;
import musicStreaming._global.event.MusicFileUpdateRequested;
import musicStreaming._global.event.MusicFileUploadRequested;
import musicStreaming._global.logger.CustomLogger;
import musicStreaming._global.logger.CustomLoggerType;

import musicStreaming.sanityCheck.reqDtos.LogsReqDto;
import musicStreaming.sanityCheck.reqDtos.MockMusicFileDeleteRequestedReqDto;
import musicStreaming.sanityCheck.reqDtos.MockMusicFileUpdateRequestedReqDto;
import musicStreaming.sanityCheck.reqDtos.MockMusicFileUploadRequestedReqDto;
import musicStreaming.sanityCheck.resDtos.LogsResDto;

@Service
@RequiredArgsConstructor
public class SanityCheckService {
    private final String logFilePath = "./logs/logback.log";

    // 출력된 로그들 중에서 끝부분 몇라인을 읽어서 반환시키기 위해서
    public LogsResDto logs(LogsReqDto logsReqDto) throws FileNotFoundException {
            List<String> logs = new ArrayList<>();

            try {
                
                CustomLogger.debug(CustomLoggerType.EFFECT, "Try to read logs", String.format("{filePath: %s}", logFilePath));
                
                Scanner myReader = new Scanner(new File(logFilePath));
                while (myReader.hasNextLine())
                {
                    String readLog = myReader.nextLine();
                    if (logsReqDto.getRegFilter().isEmpty()) logs.add(readLog);
                    else if(readLog.matches(logsReqDto.getRegFilter())) logs.add(readLog);
                }
                myReader.close();
                
                CustomLogger.debug(CustomLoggerType.EFFECT, "Read logs", String.format("{logsSize: %d}", logs.size()));

            } catch (FileNotFoundException e) {
                CustomLogger.error(e, "Error while reading logs", String.format("{filePath: %s}", logFilePath));
                throw new FileNotFoundException();
            }

            return new LogsResDto(logs.subList(Math.max(logs.size()-logsReqDto.getLineLength(), 0), logs.size()));
    }


    // Policy 테스트용으로 MusicFileUploadRequested 이벤트를 강제로 발생시키기 위해서
    public void mockMusicFileUploadRequested(MockMusicFileUploadRequestedReqDto mockData) {
        (new MusicFileUploadRequested(mockData)).publish();
    }

    // Policy 테스트용으로 MusicFileUpdateRequested 이벤트를 강제로 발생시키기 위해서
    public void mockMusicFileUpdateRequested(MockMusicFileUpdateRequestedReqDto mockData) {
        (new MusicFileUpdateRequested(mockData)).publish();
    }

    // Policy 테스트용으로 MusicFileDeleteRequested 이벤트를 강제로 발생시키기 위해서
    public void mockMusicFileDeleteRequested(MockMusicFileDeleteRequestedReqDto mockData) {
        (new MusicFileDeleteRequested(mockData)).publish();
    }
}
