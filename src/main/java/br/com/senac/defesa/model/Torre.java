package br.com.senac.defesa.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Torre {

    private final int id;
    private final String tipo;
    private final Ponto posicao;
    private final double alcance;

    private RegraDeAlvo regraDeAlvo = new MaisProximo(); // Inicia com PROXIMO por padrão
    protected int recarga;
    private double angulo;
    protected Ponto tiroAtual;

    public Torre(int id, String tipo, Ponto posicao, double alcance) {
        this.id = id;
        this.tipo = tipo;
        this.posicao = posicao;
        this.alcance = alcance;
    }

    // --- TEMPLATE METHOD ---
    public final void agir(List<Inimigo> inimigos) {
        tiroAtual = null;

        if (recarga > 0) {
            recarga--;
            return;
        }

        // Filtro de alcance unificado na Torre (Parte 2, item 4)
        List<Inimigo> candidatos = new ArrayList<>();
        for (Inimigo inimigo : inimigos) {
            if (inimigo.estaVivo() && posicao.distanciaAte(inimigo.getPosicao()) <= alcance) {
                candidatos.add(inimigo);
            }
        }

        if (candidatos.isEmpty()) {
            return;
        }

        // Escolha do alvo delegada para a Strategy
        Inimigo alvo = regraDeAlvo.selecionarAlvo(candidatos, posicao);
        if (alvo == null) {
            return;
        }

        // Passo fixo de mira
        angulo = Math.atan2(alvo.getPosicao().z() - posicao.z(), alvo.getPosicao().x() - posicao.x());

        // Passos abstratos que variam por subclasse
        disparar(alvo, inimigos);
        recarga = tempoDeRecarga();
    }

    protected abstract void disparar(Inimigo alvo, List<Inimigo> todosInimigos);
    protected abstract int tempoDeRecarga();

    // --- STRATEGY INTEGRATION ---
    public void setRegraDeAlvo(RegraDeAlvo regraDeAlvo) {
        this.regraDeAlvo = regraDeAlvo;
    }

    public String getPrioridade() {
        return regraDeAlvo.getNome();
    }

    public String getTipo() {
        return tipo;
    }

    public int getId() {
        return id;
    }

    public Ponto getPosicao() {
        return posicao;
    }

    public double getAlcance() {
        return alcance;
    }

    public double getAngulo() {
        return angulo;
    }

    public Ponto getTiroAtual() {
        return tiroAtual;
    }
}
