package br.com.senac.defesa.model;

import java.util.List;

public class Canhao extends Torre {

    public Canhao(int id, Ponto posicao) {
        super(id, "CANHAO", posicao, 4.0);
    }

    @Override
    protected void disparar(Inimigo alvo, List<Inimigo> todosInimigos) {
        alvo.receberDano(16);
        for (Inimigo outro : todosInimigos) {
            if (outro != alvo && outro.estaVivo()
                    && outro.getPosicao().distanciaAte(alvo.getPosicao()) <= 1.5) {
                outro.receberDano(5);
            }
        }
        tiroAtual = alvo.getPosicao();
    }

    @Override
    protected int tempoDeRecarga() {
        return 5;
    }
}
