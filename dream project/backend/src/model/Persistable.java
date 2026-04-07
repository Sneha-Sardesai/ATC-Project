package model;

/**
 * Interface for objects that can be persisted to the database
 * Provides a common contract for persistence operations
 */
public interface Persistable {
    int getId();
    void setId(int id);
}