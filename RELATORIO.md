# relatorio do laboratorio: junit 5

**dupla:** vicente ferreira maciel neto 598581 (so eu msm)
**link do fork:** https://github.com/v1cntz/lab-junit-jacoco-v1cntz

## 1. resultado final

```text
Test run finished after 134 ms
[        11 containers found      ]
[         0 containers skipped    ]
[        11 containers started    ]
[         0 containers aborted    ]
[        11 containers successful ]
[         0 containers failed     ]
[        58 tests found           ]
[         0 tests skipped         ]
[        58 tests started         ]
[         0 tests aborted         ]
[        58 tests successful      ]
[         0 tests failed          ]
```

## 2. nivel 1: `if`s de `Conta`

| condicao | teste que torna verdadeiro | teste que torna falso |
|---|---|---|
| saldo inicial negativo | `saldoInicialNegativoDeveVirarZero` | `contaNovaDeveGuardarTitularESaldoInicial` |
| deposito <= 0 | `depositoComValorNegativoDeveSerRecusado`, `depositoZeroDeveSerRecusado` | `depositoValidoDeveAumentarOSaldo` |
| saque <= 0 | `saqueNegativoDeveSerRecusado`, `saqueZeroDeveSerRecusado` | `saqueValidoDeveDiminuirOSaldo` |
| saque maior que saldo | `saqueMaiorQueOSaldoDeveSerRecusado` | `saqueValidoDeveDiminuirOSaldo`, `saqueDoSaldoExatoDeveSerPermitidoEZerarAConta` |

## 3. nivel 2: regras de `ServicoTransferencia`

| regra | testes |
|---|---|
| r1 mesma conta | `mesmaContaDeveSerRecusada`; `mesmaContaTemPrioridadeSobreValorInvalido` |
| r2 valor <= 0 | `transferenciaZeroDeveSerRecusada`; `transferenciaNegativaDeveSerRecusada` |
| r3 limite | `transferenciaAcimaDoLimiteDeveSerRecusada` (5000,01); `transferenciaDeExatamente5000DeveSerPermitida` (5000) |
| r4 saldo e tarifa | `saldoInsuficienteParaValorMaisTarifaNaoAlteraNenhumaConta`; `saldoExatoParaValorMaisTarifaDeveSerAceito`; `transferenciaAcimaDe1000PagaTarifaDe1Porcento`; `transferenciaDeExatamente1000NaoPagaTarifa`; `transferenciaLogoAcimaDe1000PagaTarifa` |
| r5 transferencia ok | `transferenciaPequenaDeveSerAceitaSemTarifa`; `transferenciaAcimaDe1000PagaTarifaDe1Porcento`; `saldoExatoParaValorMaisTarifaDeveSerAceito` |

## 4. nivel 3: regras de `AnalisadorEmprestimo`

| regra | cenario escolhido e resultado esperado | metodo |
|---|---|---|
| 1. dados invalidos | score -1/1001; renda 0/-1; valor 0/-1; parcelas 0/361. uma condicao invalida por caso, demais validas: dados_invalidos | `entradasInvalidasDevemSerRecusadas` |
| 2. score < 300 | score 0 e 299: negado. score 300 e prestacao baixa: analise_manual | `scoreEParcelasNosLimitesValidos` |
| 3. taxas | score 300/599: 36%; 600/799: 24%; 800/1000: 12% ao ano | `taxaAnualDeveRespeitarFaixas` |
| 4. price | 1000, 1% mensal, 1 mes: 1010; 1000, 2%, 12 meses: 94,5595966; 12000, 1%, 12 meses: 1066,1854641 | `parcelaPriceDeveTerValorEsperado` |
| 5. comprometimento | score 800, valor 1000, 1 mes: parcela 1010. renda 2019,99: negado; 2020: manual. renda 3366,66: manual; 3366,67: aprovado. score 600, parcela 1020 / renda 2040: 50%, manual | `decisaoDeveConsiderarParcelaRendaEScore`; `comprometimentoExatoDe50PorcentoDeveIrParaAnalise` |
| 6. comprometimento <= 30% | score 600, valor 1000, 1 mes, renda 3400: parcela 1020 e comprometimento 30%, aprovado. score 599, renda 10000: manual | `comprometimentoExatoDe30PorcentoDeveSerAprovado`; `decisaoDeveConsiderarParcelaRendaEScore` |

## 5. reflexao

1. **os dois lados do `if`:** faltava testar saque negativo.
   sem esse teste, o saque podia aumentar o saldo e passar.

2. **valor-limite:** sacar o saldo todo tem que funcionar.
   se trocar > por >=, ele recusa o saque e o teste pega isso.

3. **nivel 3:** os testes seguem as regras e os valores no limite.
   tem caso de dado errado, score baixo, juros e parcela alta pra renda.

4. **teste sem assert:** so chamar sacar(30) nao confere nada.
   pode tirar o valor errado e o teste passar mesmo assim.

5. **divisao do trabalho:** fiz sozinho, com ajuda do codex nos testes.
