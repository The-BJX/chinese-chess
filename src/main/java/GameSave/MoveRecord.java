package GameSave;

import data.Position;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.zip.*;


public class MoveRecord implements Serializable {
    public final Position fromPosition;
    public final Position toPosition;

    public MoveRecord(Position fromPosition, Position toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    @Override
    public String toString() {
        return fromPosition.toString() + " -> " + toPosition.toString();
    }
}