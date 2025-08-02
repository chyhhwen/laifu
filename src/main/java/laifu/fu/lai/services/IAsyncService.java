package laifu.fu.lai.services;

import java.util.concurrent.Future;

public interface IAsyncService
{
    Future<String> asyncMethod();
}
