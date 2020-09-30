package algoritmo;

import java.util.*;
import java.util.stream.Collectors;

public class Poupador extends ProgramaPoupador {

    private static String[][] caminhos = new String[30][30];

    private static Integer[] visaoAoRedor = new Integer[]{11, 12, 7, 16};

    private static Integer[] visaoMoedasAoRedor = new Integer[]{11, 10, 12, 13, 7, 2, 16, 21};

    private static POSICAO[] mapaJogadas = new POSICAO[4];

    private static Map<POSICAO, Boolean> mapsTemParede = new HashMap<>();

    private static Map<POSICAO, Boolean> mapsTemMoedas = new HashMap<>();

    private static Map<POSICAO, Boolean> mapsTemLadroes = new HashMap<>();

    private boolean preencheu = false;

    private boolean mapeou = false;

    private int cont = 0;

    private int contJogadas = 0;

    private Map<POSICAO, Object> mapaOcorrencias = new HashMap<>();

    private String texto;

    private Boolean valvulaRepetirJogada = false;

    private Integer repetirJogada;

    private enum POSICAO {
        NEUTRO(0, 0, 0), CIMA(1, 7, 2), BAIXO(2, 16, 21), ESQUERDA(4, 11, 10), DIREITA(3, 12, 13);
        private Integer n;
        private Integer codigoPosicaoVisao;
        private Integer codigoPosicaoVisaoAmpliada;

        private POSICAO(Integer n, Integer posicaoVisao, Integer codigoPosicaoVisaoAmpliada) {
            this.codigoPosicaoVisao = posicaoVisao;
            this.codigoPosicaoVisaoAmpliada = codigoPosicaoVisaoAmpliada;
            this.n = n;
        }

        public Integer getValor() {
            return n;
        }

        public Integer getCodigoPosicaoVisao() {
            return codigoPosicaoVisao;
        }

        public Integer getCodigoPosicaoVisaoAmpliada() {
            return codigoPosicaoVisaoAmpliada;
        }
    }


    public int acao() {
        contJogadas++;
        cont++;
        if (cont == 4) {
            mapeou = true;
        }

        if (!preencheu) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    caminhos[i][j] = "0";
                }
            }
            preencheu = true;
        }


        atualizarVisao();
        mapearCaminhos(sensor);
        if (!mapeou) {
            return (int) (Math.random() * 5);
        }

        if (temParedeEmUmaDirecao(sensor)) {
            return getPosicaoParaParedeEmUmaDirecao(sensor).getValor();
        }

        if (temParedeEmMaisDeUmaDirecao(sensor)) {
            return getPosicaoParaParedeEmMaisDeUmaDirecao(sensor).getValor();
        }

        if (temMoedasAoRedor(sensor)) {
            return getPosicaoMoeda(sensor).getValor();
        }

        int menosPassagens = verificarOrigem(sensor);
        return (int) getPosicaoDestino(sensor, menosPassagens).getValor();
    }

    private POSICAO getPosicaoMoeda(SensoresPoupador sensoresPoupador) {
        for (Integer nVision : visaoAoRedor) {
            if (sensoresPoupador.getVisaoIdentificacao()[nVision] == 4) {
                return getPosicaoPorCodigo(nVision);
            }
        }
        return POSICAO.NEUTRO;
    }

    private Boolean temMoedasAoRedor(SensoresPoupador sensoresPoupador) {
        for (Integer nVision : visaoAoRedor) {
            if (sensoresPoupador.getVisaoIdentificacao()[nVision] == 4) {
                mapsTemParede.put(getPosicaoPorCodigo(nVision), true);
                mapearParede(nVision);
                return true;
            }
        }
        return false;
    }

    private Boolean temParedeEmUmaDirecao(SensoresPoupador sensoresPoupador) {
        Set<POSICAO> posicoes = new HashSet<>();
        for (Integer nVision : visaoAoRedor) {
            if (sensoresPoupador.getVisaoIdentificacao()[nVision] == 1) {
                posicoes.add(getPosicaoPorCodigo(nVision));
            }
        }
        return posicoes.size() == 1;
    }

    private POSICAO getPosicaoParaParedeEmUmaDirecao(SensoresPoupador sensoresPoupador) {
        List<POSICAO> posicoesEncurralado = new ArrayList<>();
        for (Integer nVision : visaoAoRedor) {
            if ((sensoresPoupador.getVisaoIdentificacao()[13] == 1  || sensoresPoupador.getVisaoIdentificacao()[11] == 1) &&  (sensoresPoupador.getVisaoIdentificacao()[12] == 1  || sensoresPoupador.getVisaoIdentificacao()[10] == 1)) {
                System.out.print("encurralado");
            }

            if ((sensoresPoupador.getVisaoIdentificacao()[21] == 1  || sensoresPoupador.getVisaoIdentificacao()[7] == 1) &&  (sensoresPoupador.getVisaoIdentificacao()[12] == 1  || sensoresPoupador.getVisaoIdentificacao()[10] == 1)) {
                System.out.print("encurralado");
            }

            if (sensoresPoupador.getVisaoIdentificacao()[nVision] == 1) {
                if (temMoedasAoRedor(sensoresPoupador)) {
                    return getPosicaoMoeda(sensoresPoupador);
                } else if (getLadoExtremidade(sensoresPoupador) != POSICAO.NEUTRO | getAoLadoExtremidade(sensoresPoupador) != POSICAO.NEUTRO) {
                    if (getLadoExtremidade(sensoresPoupador) != POSICAO.NEUTRO) {
                        posicoesEncurralado.add(getLadoExtremidade(sensoresPoupador));
                    } else {
                        posicoesEncurralado.add(getAoLadoExtremidade(sensoresPoupador));
                    }

                    posicoesEncurralado.add(getPosicaoPorCodigo(nVision));
                    List<POSICAO> posicoesEscolherUma = Arrays.asList(POSICAO.values());
                    posicoesEscolherUma = posicoesEscolherUma.stream().distinct().filter(p -> !posicoesEncurralado.contains(p) | !p.equals(POSICAO.NEUTRO)).collect(Collectors.toList());
                    return posicoesEscolherUma.get(0);
                } else {
                    return getPosicaoContraria(getPosicaoPorCodigo(nVision));
                }
            }


        }

        return POSICAO.NEUTRO;
    }


    private Boolean temParedeEmMaisDeUmaDirecao(SensoresPoupador sensoresPoupador) {
        Set<POSICAO> posicoes = new HashSet<>();
        for (Integer nVision : visaoAoRedor) {
            if (sensoresPoupador.getVisaoIdentificacao()[nVision] == 1) {
                posicoes.add(getPosicaoPorCodigo(nVision));
            }
        }
        return posicoes.size() > 1;
    }

    Set<POSICAO> paredes = new HashSet<>();

    private POSICAO getPosicaoParaParedeEmMaisDeUmaDirecao(SensoresPoupador sensoresPoupador) {
        for (Integer nVision : visaoAoRedor) {
            if (sensoresPoupador.getVisaoIdentificacao()[nVision] == 1) {
                paredes.add(getPosicaoPorCodigo(nVision));
            }
        }

        if (temMoedasAoRedor(sensoresPoupador)) {
            return getPosicaoMoeda(sensoresPoupador);
        } else {
            List<POSICAO> posicoesParedes = new ArrayList<>(paredes);
            return posicoesParaEscolha(posicoesParedes).get(0);
        }

    }

    private List<POSICAO> posicoesParaEscolha(List<POSICAO> notIn) {
        List<POSICAO> not = new ArrayList<>();
            for (POSICAO no : Arrays.asList(POSICAO.values())) {
                if (!notIn.contains(no)) {
                    not.add(no);
                }
            }
        return not;
    }

    private Integer obterDestinoBaseadoEmParedes() {
        POSICAO efetiva;
        POSICAO posicaoQueTemParede;
        for (Map.Entry<POSICAO, Boolean> posicaoMapeadaParede : mapsTemParede.entrySet()) {
            if (posicaoMapeadaParede.getValue()) {
                posicaoQueTemParede = posicaoMapeadaParede.getKey();
                POSICAO paraOndeIr = getPosicaoContraria(posicaoQueTemParede);
                int menosPassagens = verificarOrigem(sensor);
                POSICAO paraOndeFazSentido = getPosicaoDestino(sensor, menosPassagens);

                if (podeIrPosicaoContraria(paraOndeIr, sensor)) {
                    efetiva = paraOndeIr;
                } else {
                    efetiva = paraOndeFazSentido;
                }

                mapaJogadas[contJogadas] = efetiva;
                atualizarVisao();
                return efetiva.getValor();
            }
        }
        return (int) (Math.random() * 5);
    }

    private void atualizarVisao() {
        if (contJogadas > 3) {
            contJogadas = 0;
            mapsTemParede.clear();
            mapsTemMoedas.clear();
            mapsTemLadroes.clear();
        }
    }

    private Boolean podeIrPosicaoContraria(POSICAO paraOndeIr, SensoresPoupador sensoresPoupador) {
        return !mapaJogadasVazio() && !Arrays.asList(mapaJogadas).contains(paraOndeIr);
    }

    private Integer getPosicaoFuga() {
        List<POSICAO> posicoesSemLadrao = new ArrayList();
        for (POSICAO posicaoComun : POSICAO.values()) {
            if (!mapsTemLadroes.containsKey(posicaoComun) || posicaoComun != POSICAO.NEUTRO) {
                posicoesSemLadrao.add(posicaoComun);
            }
        }
        return posicoesSemLadrao.get(new Random().nextInt(posicoesSemLadrao.size())).getValor();
    }

    private Integer verificarOrigem(SensoresPoupador sensor) { // verifica os caminhos que ele menos andou
        List<Integer> posicoes = Arrays.asList(getElementoPosicao(sensor, POSICAO.CIMA), getElementoPosicao(sensor, POSICAO.ESQUERDA), getElementoPosicao(sensor, POSICAO.DIREITA), getElementoPosicao(sensor, POSICAO.BAIXO));
        Collections.sort(posicoes);
        return posicoes.get(0);
    }

    private void definirRotaQuandoEncurraladoPorParede(SensoresPoupador sensor) {
        List<Integer> posicoes = Arrays.asList(getElementoPosicao(sensor, POSICAO.CIMA), getElementoPosicao(sensor, POSICAO.ESQUERDA), getElementoPosicao(sensor, POSICAO.DIREITA), getElementoPosicao(sensor, POSICAO.BAIXO));
    }

    private void mapearCaminhos(SensoresPoupador sensor) {
        int x = (int) sensor.getPosicao().getY();
        int y = (int) sensor.getPosicao().getX();
        Integer n = Integer.parseInt(caminhos[x][y]) + 1;
        caminhos[x][y] = n.toString();
    }

    private void mostrarCaminhos() {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                System.out.print(caminhos[i][j]);
            }
            System.out.println("");
        }
    }

    private POSICAO getPosicaoDestino(SensoresPoupador sensor, Object valor) {
        POSICAO p = null;
        for (Map.Entry<POSICAO, Object> item : mapaOcorrencias.entrySet()) {
            if (item.getValue().toString().equals(valor.toString())) {
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

        mapaOcorrencias.put(p, caminhos[x][y].toString());
        return Integer.parseInt(caminhos[x][y]);
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

    private POSICAO getAoLadoExtremidade(SensoresPoupador sensoresPoupador) {
        int y = (int) sensor.getPosicao().getX();
        int x = (int) sensor.getPosicao().getY();

        if (y == 28) return POSICAO.DIREITA;
        if (y == 1) return POSICAO.ESQUERDA;

        if (x == 1) return POSICAO.CIMA;
        if (x == 28) return POSICAO.BAIXO;

        return POSICAO.NEUTRO;
    }

    private POSICAO getPosicaoPorCodigo(Integer codigo) {
        for (POSICAO p : POSICAO.values()) {
            if (p.getCodigoPosicaoVisao() == codigo || p.getCodigoPosicaoVisaoAmpliada() == codigo) {
                return p;
            }
        }
        return POSICAO.NEUTRO;
    }

    private POSICAO getPosicaoContraria(POSICAO posicao) {
        if (posicao.equals(POSICAO.ESQUERDA)) return POSICAO.DIREITA;
        if (posicao.equals(POSICAO.DIREITA)) return POSICAO.ESQUERDA;
        if (posicao.equals(POSICAO.CIMA)) return POSICAO.BAIXO;
        if (posicao.equals(POSICAO.BAIXO)) return POSICAO.CIMA;
        return POSICAO.NEUTRO;
    }

    private Boolean mapaJogadasVazio() {
        boolean vazio = true;
        for (POSICAO posicao : mapaJogadas) {
            if (posicao != null) {
                vazio = false;
                break;
            }
        }
        return vazio;
    }

    private POSICAO posicaoMaisJogada() {
        POSICAO posicaoEncontrada = POSICAO.NEUTRO;
        Map<POSICAO, Integer> todasAsPosicoes = new HashMap<>();
        for (POSICAO posicao : mapaJogadas) {
            todasAsPosicoes.put(posicao, quantidadeVezesJogadas(posicao));
        }

        List<Integer> quantidades = new ArrayList<>(todasAsPosicoes.values());
        Collections.sort(quantidades);
        Integer maiorRepeticao = quantidades.get(0);

        for (Map.Entry<POSICAO, Integer> item : todasAsPosicoes.entrySet()) {
            if (item.getValue() == maiorRepeticao) {
                posicaoEncontrada = item.getKey();
            }
        }

        return posicaoEncontrada;
    }

    private Integer quantidadeVezesJogadas(POSICAO jogaPosicao) {
        Integer count = Math.toIntExact(Arrays.asList(mapaJogadas).stream().filter(jogada -> jogada.equals(jogaPosicao)).count());
        return count;
    }

    private void mapearParede(Integer nVision) {
        int x = (int) sensor.getPosicao().getY();
        int y = (int) sensor.getPosicao().getX();

        POSICAO p = getPosicaoPorCodigo(nVision);
        if (p == POSICAO.CIMA && x > 0) {
            x = x - 1;
        } else if (p == POSICAO.BAIXO && x < 29) {
            x = x + 1;
        } else if (p == POSICAO.ESQUERDA && y > 0) {
            y = y - 1;
        } else if (p == POSICAO.DIREITA && y < 29) {
            y = y + 1;
        }

        caminhos[x][y] = "9";
    }

}