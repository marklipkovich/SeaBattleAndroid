package com.mark.seabattle.presenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
  private List<Integer> area;
  private List<Boolean> hit;
  private int number;

  int getNumber() {
    return number;
  }
  List<Integer> getArea() {
    return area;
  }

  List<Boolean> getHit() {
    return hit;
  }

  public Ship(List<Integer> a, int n) {
    area = a;
    number = n;
    hit = new ArrayList<>();
     for (int ignored : area){
       hit.add(false);
     }
  }

  String shot(int cell) {
    int i = area.indexOf(cell);
    if (i == -1) {
      return "MISS";
    }
    else {
      hit.set(i, true);
    }
    if (hit.contains(false)) {
      return "HIT";
    }
    else {
      return "KILLED";
    }
  }
}
