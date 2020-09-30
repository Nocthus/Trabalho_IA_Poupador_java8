package algoritmo;

import java.util.*;

public class Poupador extends ProgramaPoupador {

    private static Integer[][] caminhos = new Integer[30][30];

    private boolean preencheu = false;

    private boolean mapeou = false;

    private int cont = 0;
    
    private int parede=100;

    private Map<POSICAO, Integer> mapaOcorrencias = new HashMap<>();
    
    private String texto;

    private enum POSICAO {
        NEUTRO(0), CIMA(1), BAIXO(2), ESQUERDA(4), DIREITA(3);

        private Integer n;

        private POSICAO(Integer n) {
            this.n = n;
        }

        public Integer getValor() {
            return n;
        }
    }


    public int acao() {
        cont++;

        if (cont == 4) {
            mapeou = true;
        }

        if (!preencheu) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    caminhos[i][j] = 0;
                }
            }
            preencheu = true;
        }

        mapearCaminhos(sensor);

        if (sensor.getVisaoIdentificacao()[7] >= 200 || sensor.getVisaoIdentificacao()[2] >= 200) { // se o que tem em cima é ladrão
            if (sensor.getVisaoIdentificacao()[16] != 1 && sensor.getVisaoIdentificacao()[16] != 200) // diferente de uma parete
                return 2;
            else if (sensor.getVisaoIdentificacao()[11] != 1 && sensor.getVisaoIdentificacao()[11] != 200)
                return 4;
            else if (sensor.getVisaoIdentificacao()[12] != 1 && sensor.getVisaoIdentificacao()[12] != 200)
                return 3;

        } else if (sensor.getVisaoIdentificacao()[11] >= 200 || sensor.getVisaoIdentificacao()[10] >= 200) {
            //fuga
            if (sensor.getVisaoIdentificacao()[12] != 1 && sensor.getVisaoIdentificacao()[12] != 200)
                return 3;
            else if (sensor.getVisaoIdentificacao()[7] != 1 && sensor.getVisaoIdentificacao()[7] != 200)
                return 1;
            else if (sensor.getVisaoIdentificacao()[16] != 1 && sensor.getVisaoIdentificacao()[16] != 200)
                return 2;


        } else if (sensor.getVisaoIdentificacao()[12] >= 200 || sensor.getVisaoIdentificacao()[13] >= 200) {
            //fuga
            if (sensor.getVisaoIdentificacao()[11] != 1 && sensor.getVisaoIdentificacao()[11] != 200)
                return 4;
            else if (sensor.getVisaoIdentificacao()[7] != 1 && sensor.getVisaoIdentificacao()[7] != 200)
                return 1;
            else if (sensor.getVisaoIdentificacao()[16] != 1 && sensor.getVisaoIdentificacao()[16] != 200)
                return 2;

        } else if (sensor.getVisaoIdentificacao()[16] >= 200 || sensor.getVisaoIdentificacao()[21] >= 200) {
            //fuga
            if (sensor.getVisaoIdentificacao()[7] != 1 && sensor.getVisaoIdentificacao()[7] != 200)
                return 1;
            else if (sensor.getVisaoIdentificacao()[11] != 1 && sensor.getVisaoIdentificacao()[11] != 200)
                return 4;
            else if (sensor.getVisaoIdentificacao()[12] != 1 && sensor.getVisaoIdentificacao()[12] != 200)
                return 3;
        }

        //Moedas
        else if (sensor.getVisaoIdentificacao()[7] == 4) {
            return 1;
        } else if (sensor.getVisaoIdentificacao()[11] == 4) {
            return 4;
        } else if (sensor.getVisaoIdentificacao()[12] == 4) {
            return 3;
        } else if (sensor.getVisaoIdentificacao()[16] == 4) {
            return 2;
        }


		else if(sensor.getVisaoIdentificacao()[7] == 1){
			return 2;
		} else if(sensor.getVisaoIdentificacao()[11] == 1){
			return 3;
		} else if(sensor.getVisaoIdentificacao()[12] == 1) {
			return 4;
		} else if(sensor.getVisaoIdentificacao()[16] == 1){
			return 1;
		}

        //Banco
        /*
        else if (sensor.getVisaoIdentificacao()[7] == 3) { // CIMA
            return 1;
        } else if (sensor.getVisaoIdentificacao()[11] == 3) { // ESQUERDA
            return 4;
        } else if (sensor.getVisaoIdentificacao()[12] == 3) { // DIREITA
            return 3;
        } else if (sensor.getVisaoIdentificacao()[16] == 3) { // BAIXO
            return 2;
        }*/

        if (!mapeou) {
            return (int) (Math.random() * 5);
        }

        mostrarCaminhos();
        int menosPassagens = verificarOrigem(sensor);
        return (int) getPosicaoDestino(sensor, menosPassagens).getValor();
    }

    private Integer  verificarOrigem(SensoresPoupador sensor) {
        List<Integer> posicoes = Arrays.asList(getElementoPosicao(sensor, POSICAO.CIMA), getElementoPosicao(sensor, POSICAO.ESQUERDA), getElementoPosicao(sensor, POSICAO.DIREITA), getElementoPosicao(sensor, POSICAO.BAIXO));
        Collections.sort(posicoes);
        return posicoes.get(0);
    }

    private void mapearCaminhos(SensoresPoupador sensor) {
        int x = (int) sensor.getPosicao().getY();
        int y = (int) sensor.getPosicao().getX();
        caminhos[x][y] = caminhos[x][y] + 1;
    }

    private void mostrarCaminhos() {
        for (int i = 0; i < 30; i++) {
            for (int j=0; j < 30; j++) {
                System.out.print(caminhos[i][j]);
            }
            System.out.println("");
        }
    }

    private POSICAO getPosicaoDestino(SensoresPoupador sensor, Integer valor) {
        POSICAO p = null;
        for (Map.Entry<POSICAO, Integer> item : mapaOcorrencias.entrySet()) {
            if (item.getValue() == valor) {
                p = item.getKey();

                if (p == getLadoExtremidade(sensor)) {
                    continue;
                } else {
                    break;
                }
            }
        }
        return p;
    }

    private int getElementoPosicao(SensoresPoupador sensor, POSICAO p) {
        int x = (int) sensor.getPosicao().getY();
        int y = (int) sensor.getPosicao().getX();

        if (p == POSICAO.CIMA && x > 0) {
            x = x - 1;
        } else if (p == POSICAO.BAIXO && x < 29) {
            x = x + 1;
        } else if (p == POSICAO.ESQUERDA && y > 0) {
            y = y - 1;
        } else if (p == POSICAO.DIREITA && y < 29) {
            y = y + 1;
        }

        mapaOcorrencias.put(p,  caminhos[x][y]);
        return caminhos[x][y];
    }

    private POSICAO getLadoExtremidade(SensoresPoupador sensoresPoupador) {
        int y = (int) sensor.getPosicao().getX();
        int x = (int) sensor.getPosicao().getY();

        if (y == 29) return POSICAO.DIREITA;
        if (y == 0) return POSICAO.ESQUERDA;

        if (x == 0) return POSICAO.CIMA;
        if (x == 29) return POSICAO.BAIXO;

        return POSICAO.NEUTRO;
    }



}