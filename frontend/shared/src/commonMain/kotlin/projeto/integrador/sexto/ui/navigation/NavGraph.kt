package projeto.integrador.sexto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import projeto.integrador.sexto.di.ProvedorDependencias
import projeto.integrador.sexto.ui.screens.alerta.AlertaFormularioTela
import projeto.integrador.sexto.ui.screens.aquario.AquarioFormularioTela
import projeto.integrador.sexto.ui.screens.cadastro.CadastroTela
import projeto.integrador.sexto.ui.screens.comprador.CompradorFormularioTela
import projeto.integrador.sexto.ui.screens.configuracaotemperatura.ConfiguracaoTemperaturaFormularioTela
import projeto.integrador.sexto.ui.screens.dispositivoiot.DispositivoIoTFormularioTela
import projeto.integrador.sexto.ui.screens.especie.EspecieFormularioTela
import projeto.integrador.sexto.ui.screens.especime.EspecimeFormularioTela
import projeto.integrador.sexto.ui.screens.home.HomeTela
import projeto.integrador.sexto.ui.screens.leituratemperatura.LeituraTemperaturaFormularioTela
import projeto.integrador.sexto.ui.screens.login.LoginTela
import projeto.integrador.sexto.ui.screens.loja.LojaFormularioTela
import projeto.integrador.sexto.ui.screens.programacaoAlimentacao.ProgramacaoAlimentacaoFormularioTela
import projeto.integrador.sexto.ui.screens.venda.VendaFormularioTela

class AppNavController {

    val pilha: NavBackStack<RotaApp> = NavBackStack<RotaApp>(RotaApp.Login())

    fun navegarPara(rota: RotaApp) {
        pilha.add(rota)
    }

    fun voltar(): Boolean {
        return if (pilha.size > 1) {
            pilha.removeLast()
            true
        } else {
            false
        }
    }

    fun navegarELimpar(rota: RotaApp) {
        pilha.clear()
        pilha.add(rota)
    }
}

@Composable
fun AppNavHost(navController: AppNavController) {
    NavDisplay(
        backStack = navController.pilha,
        entryProvider = entryProvider<NavKey> {

            entry<RotaApp.Login> {
                LoginTela(
                    aoLogarComSucesso = { nomeUsuario: String ->
                        navController.navegarELimpar(RotaApp.Home(nomeUsuario))
                    },
                    aoNavegarParaCadastro = {
                        navController.navegarPara(RotaApp.Cadastro)
                    }
                )
            }

            entry<RotaApp.Cadastro> {
                CadastroTela(
                    aoVoltarParaLogin = {
                        navController.voltar()
                    },
                    aoRegistrarComSucesso = { nomeUsuario: String ->
                        val mensagem = "Cadastro concluido, $nomeUsuario! Efetue login abaixo."
                        navController.navegarELimpar(RotaApp.Login(mensagemExito = mensagem))
                    }
                )
            }

            entry<RotaApp.Home> { rota ->
                HomeTela(
                    nomeUsuario = rota.nomeUsuario,
                    aoSair = {
                        ProvedorDependencias.authStore.limparSessao()
                        navController.navegarELimpar(RotaApp.Login())
                    },
                    aoSelecionarEntidade = { entidade ->
                        when (entidade) {
                            "Configuracoes de Temperatura" ->
                                navController.navegarPara(RotaApp.FormularioConfiguracaoTemperatura)
                            "Leituras de Temperatura" ->
                                navController.navegarPara(RotaApp.FormularioLeituraTemperatura)
                            "Cadastrar Loja" ->
                                navController.navegarPara(LojaRotas.formulario)
                            "Aquarios" ->
                                navController.navegarPara(AquarioRotas.formulario)
                            "Compradores" ->
                                navController.navegarPara(RotaApp.FormularioComprador)
                            "Programacoes de Alimentacao" ->
                                navController.navegarPara(RotaApp.FormularioProgramacaoAlimentacao)
                            "Vendas" ->
                                navController.navegarPara(RotaApp.FormularioVenda)
                            "Especies" ->
                                navController.navegarPara(RotaApp.FormularioEspecie)
                            "Dispositivos IoT" ->
                                navController.navegarPara(RotaApp.FormularioDispositivoIoT)
                            "Especimes" ->
                                navController.navegarPara(RotaApp.FormularioEspecime)
                            "Alertas" ->
                                navController.navegarPara(RotaApp.FormularioAlerta)
                        }
                    }
                )
            }

            entry<LojaRotas.formulario> {
                LojaFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<AquarioRotas.formulario> {
                AquarioFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioConfiguracaoTemperatura> {
                ConfiguracaoTemperaturaFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioEspecime> {
                EspecimeFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioEspecie> {
                EspecieFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioLeituraTemperatura> {
                LeituraTemperaturaFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioComprador> {
                CompradorFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioProgramacaoAlimentacao> {
                ProgramacaoAlimentacaoFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioVenda> {
                VendaFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioDispositivoIoT> {
                DispositivoIoTFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }

            entry<RotaApp.FormularioAlerta> {
                AlertaFormularioTela(
                    aoVoltar = { navController.voltar() }
                )
            }
        }
    )
}
