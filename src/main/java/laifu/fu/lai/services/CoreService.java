package laifu.fu.lai.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class CoreService implements ICoreService
{
    private final AsyncServices asyncService;

    public CoreService(AsyncServices asyncService) {
        this.asyncService = asyncService;
    }

    private Future<String> futureAsyncMethod = null;

    @Override
    public String run() {
        if (futureAsyncMethod == null)
            futureAsyncMethod = asyncService.asyncMethod();

        if (futureAsyncMethod.isDone()) {
            String result = "";
            try {
                result = futureAsyncMethod.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            futureAsyncMethod = null;
            return result;
        } else {
            return "run";
        }
    }
}
