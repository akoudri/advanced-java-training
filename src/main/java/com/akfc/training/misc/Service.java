package com.akfc.training.misc;

public class Service {
    private Repository repository;

    public Service(Repository mockRepository) {
        this.repository = mockRepository;
    }

    public String performAction() {
        return repository.getData();
    }

    public int performComputation(int x) {
        return repository.square(x) + 10;
    }

    public int square(int x) {
        return x * x;
    }
}
