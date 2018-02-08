package io.silverspoom.bulldog.minnowboard;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.BoardFactory;
import io.silverspoon.bulldog.linux.sysinfo.CpuInfo;

public class MinnowBoardFactory implements BoardFactory {
    @Override
    public boolean isCompatibleWithPlatform() {
        // if intel chip is present
        return CpuInfo.getHardware().contains("GenuineIntel");
    }

    @Override
    public Board createBoard() {
        return new MinnowBoard();
    }
}
