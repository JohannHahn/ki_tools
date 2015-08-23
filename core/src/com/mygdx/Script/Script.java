/*
 * Riho Peterson 2014
 * tulevik.EU
 * http://www.indiedb.com/games/office-management-101
 */
package com.mygdx.Script;

public interface Script {
  boolean canExecute();
  boolean executeInit(Object... objects);
  boolean executeFunction(String functionName, Object... objects);
  boolean reload();
}
