package plainjava.src.domain.hackathon.model;

import java.io.File;

public class Submission {
    
    private File data;
    private Valuation valuation;

    public Submission(File data) {
        this.data = data;
    }

    public void setValuation(Valuation valuation) {
        this.valuation = valuation;
    }

    public Valuation getValuation() {
        return valuation;
    }
}
