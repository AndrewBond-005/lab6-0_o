package util;

import models.Worker;

import java.util.List;
import java.io.Serializable;
// в команду
public record Request(String command, List<String> args, List<Worker> workers) implements Serializable  {
}
