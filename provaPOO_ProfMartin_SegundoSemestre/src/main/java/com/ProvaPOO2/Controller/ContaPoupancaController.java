package com.ProvaPOO2.Controller;

import java.io.IOException;

import com.ProvaPOO2.DAO.ClienteDAO;
import com.ProvaPOO2.DAO.ContaCorrenteDAO;
import com.ProvaPOO2.DAO.ContaPoupancaDAO;
import com.ProvaPOO2.model.Cliente;
import com.ProvaPOO2.model.ContaCorrente;
import com.ProvaPOO2.model.ContaPoupanca;
import com.ProvaPOO2.util.GeradorDeContas;
import com.ProvaPOO2.util.TextFieldFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ContaPoupancaController {

	// CRIA NOVA CONTA POUPANCA
	@FXML
	private Button btnCriarCp;

	@FXML
	private TextField txtCpfClienteNewCp;

	@FXML
	private PasswordField txtSenhaNewCp;

	@FXML
	private TextField txtDepInicialCp;

	@FXML
	void tfMaskCpf() {
		TextFieldFormatter tff = new TextFieldFormatter();
		tff.setMask("###.###.###-##");
		tff.setCaracteresValidos("0123456789");
		tff.setTf(txtCpfClienteNewCp);
		tff.formatter();

	}

	@FXML
	void novaContaPoupanca(ActionEvent event) {
		String cpf = txtCpfClienteNewCp.getText();
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		if(txtDepInicialCp.getText().equals("")) {
			alerta("Error", "Preencha o campo valor!", "Campo nulos identificados!!!");
			return;
		}
		double valor = Double.parseDouble(txtDepInicialCp.getText());
		String senha = txtSenhaNewCp.getText();

		if (cpf.equals("") || senha.equals("")) {
			alerta("Error", "Preencha os campos!", "Campo nulos identificados!!!");
		} else {
			ContaPoupancaDAO ccDao = new ContaPoupancaDAO();
			ClienteDAO cliDao = new ClienteDAO();
			Cliente cliente = cliDao.buscaClienteByCPF(cpf);
			if (!cpf.equals("")) {
				try {
					if (cliDao.buscaClienteByCPF(cpf) != null) {
						ContaPoupanca cp = new ContaPoupanca();
						cp.setAgencia(9929);
						cp.setNumConta(GeradorDeContas.geraCp());
						cp.setSaldo(valor);
						cp.setIdCliente(cliente.getId());
						cp.setSenha(senha);
						ccDao.inserirCp(cp);
						cliDao.alteraStatus(cpf);
						alerta("Sucesso!!!", "Parab�ns!", "Conta aberta com sucesso!!!");
						FXMLLoader fxmlLoader = new FXMLLoader(
								TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));
						Parent root1 = fxmlLoader.load();
						Stage stage = new Stage();
						stage.setScene(new Scene(root1));
						stage.initStyle(StageStyle.UNDECORATED);
						stage.show();
						fecharTela(btnCriarCp);
					} else {
						alerta("Erro!!!", "O cpf informado n�o � de um cliente!",
								"Antes de criar uma conta, crie um cliente!!!");
					}

				} catch (Exception e) {

				}
			}
		}

	}

	// DEPOSITO

	@FXML
	private TextField txtNumContaCp;

	@FXML
	private TextField txtValorCpDep;

	@FXML
	private TextField txtSenha;

	@FXML
	private Button txtDepositaCp;

	@FXML
	void depositaCp(ActionEvent event) {
		String conta = txtNumContaCp.getText();
		if(txtValorCpDep.getText().equals("")) {
			alerta("Error", "Preencha o campo valor!", "Campo nulos identificados!!!");
			return;
		}
		double valor = Double.parseDouble(txtValorCpDep.getText());

		if (conta.equals("")) {
			alerta("Error", "Preencha os campos!", "Campo nulos identificados!!!");
		} else {
			ContaPoupancaDAO cpDao = new ContaPoupancaDAO();
			ContaPoupanca cp = cpDao.findByConta(conta);
			if (conta == "") {
				alerta("Error!!!", "O deposito falhou!", "Por favor preencha o formulario!!!");
			} else {
				if (cp != null) {
					try {
						cpDao.depositaCP(cp, valor);
						alerta("Sucesso!!!", "Deposito!", "Deposito realizado com sucesso!!!");
						FXMLLoader fxmlLoader = new FXMLLoader(
								TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));
						Parent root1 = fxmlLoader.load();
						Stage stage = new Stage();
						stage.setScene(new Scene(root1));
						stage.initStyle(StageStyle.UNDECORATED);
						stage.show();
						fecharTela(txtDepositaCp);
					} catch (IOException e) {

					}
				} else {

					alerta("Error!!!", "O deposito falhou!", "A conta informada n�o existe!!!");
				}
			}

		}

	}

	// SACAR
	@FXML
	private Button btnSacar;

	@FXML
	private TextField txtNumContaSacar;

	@FXML
	private TextField txtValorSaque;
	@FXML
	private PasswordField txtSenhaSaque;

	@FXML
	void sacarCp(ActionEvent event) {
		String conta = txtNumContaSacar.getText();
		if(txtValorSaque.getText().equals("")) {
			alerta("Error", "Preencha o campo valor!", "Campo nulos identificados!!!");
			return;
		}
		double valor = Double.parseDouble(txtValorSaque.getText());
		String senha = txtSenhaSaque.getText();

		if (conta.equals("") || senha.equals("")) {
			alerta("Error", "Preencha os campos!", "Campo nulos identificados!!!");
		} else {
			ContaPoupancaDAO cpDao = new ContaPoupancaDAO();
			ContaPoupanca cp = cpDao.findByConta(conta);
			if(cp == null) {
				alerta("Error!!!", "Saque falhou!", "A conta n�o existe.");
				return;
			}
			if (cpDao.autentica(conta, senha)) {
				System.out.println("Autenticado com sucesso!!!");
				if (cp.getSaldo() < valor) {

					alerta("Aten��o!!!", "Saque falhou!", "O saldo � insuficiente para o saque.");
				} else {
					try {
						cpDao.sacarCP(cp, valor);
						alerta("Sucesso!!!", "Saque realizado!", "O dinheiro j� foi debitado.");
						FXMLLoader fxmlLoader = new FXMLLoader(
								TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));
						Parent root1 = fxmlLoader.load();
						Stage stage = new Stage();
						stage.setScene(new Scene(root1));
						stage.initStyle(StageStyle.UNDECORATED);
						stage.show();
						fecharTela(btnSacar);
					} catch (IOException e) {

					}

				}

			} else {
				alerta("Error!!!", "Autentica��o falhou!", "A senha esta incorreta ou sua conta n�o existem.");
			}
		}

	}

	//////////// TRANSFERENCIA////////////
	@FXML
	private Button btnTranferirCp;

	@FXML
	private Button btnSair;

	@FXML
	private TextField txtcontaOrigem;

	@FXML
	private TextField txtvalorOrigem;

	@FXML
	private TextField txtcontaDestino;
	@FXML
	private PasswordField txtSenhaTranferencia;

	@FXML
	void tranferir(ActionEvent event) {
		String contaOrigem = txtcontaOrigem.getText();
		String contaDestino = txtcontaDestino.getText();
		if(txtvalorOrigem.getText().equals("")) {
			alerta("Error", "Preencha o campo valor!", "Campo nulos identificados!!!");
			return;
		}
		Double valor = Double.parseDouble(txtvalorOrigem.getText());
		String senha = txtSenhaTranferencia.getText();
		if (contaOrigem.equals("") || senha.equals("") || contaDestino.equals("")) {
			alerta("Error", "Preencha os campos!", "Campo nulos identificados!!!");
		} else {
			ContaPoupancaDAO cpDao = new ContaPoupancaDAO();
			ContaPoupanca cpo = cpDao.findByConta(contaOrigem);
			ContaPoupanca cpd = cpDao.findByConta(contaDestino);
			if (cpo == null) {
				alerta("Error!!!", "Conta inexistente!", "A conta de origem n�o existe.");
			} else {
				if (contaDestino.equals(contaOrigem)) {
					alerta("Error!!!", "Conta identicas!", "Voc� n�o pode tranferir para mesma conta.");

				} else {
					if (cpDao.autentica(contaOrigem, senha)) {
						System.out.println("Autenticado com sucesso!!!");
						if (cpd == null) {
							alerta("Error!!!", "Tranferencia falhou!", "A conta destino n�o existe.");
						} else {
							if (cpo.getSaldo() < valor) {
								alerta("Aten��o!!!", "Tranferencia falhou!",
										"O saldo � insuficiente para a transferencia.");

							} else {
								try {
									cpDao.sacarCP(cpo, cpDao.tarifaTransferencia(valor));
									cpDao.depositaCP(cpd, valor);
									alerta("Sucesso!!!", "Tranferencia realizada com sucesso!",
											"Tudo certo, o dinheiro est� a caminho do destinatario!");
									FXMLLoader fxmlLoader = new FXMLLoader(
											TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));
									Parent root1 = fxmlLoader.load();
									Stage stage = new Stage();
									stage.setScene(new Scene(root1));
									stage.initStyle(StageStyle.UNDECORATED);
									stage.show();
									fecharTela(btnTranferirCp);
								} catch (IOException e) {

								}

							}

						}

					} else {
						alerta("Error!!!", "Autentica��o falhou!", "A senha esta incorreta ou sua conta n�o existe.");
					}

				}

			}
		}

	}

	// TRANFERENTE P/ CONTA POUPAN�A

	@FXML
	private TextField txtContaPoupancaTranfereCCorrente;

	@FXML
	private TextField txtCCorrenteTranfereContaPoupanca;

	@FXML
	private TextField valorTranferenciaentreCcCp;

	@FXML
	private PasswordField txtPassCCcp;

	@FXML
	private Button btnTransfereCcCp;

	@FXML
	void transferenciaEntreContasCpParaCc(ActionEvent event) {

		String cCorrente = txtCCorrenteTranfereContaPoupanca.getText();
		String cPoupanca = txtContaPoupancaTranfereCCorrente.getText();
		if(txtPassCCcp.getText().equals("")) {
			alerta("Error", "Preencha o campo valor!", "Campo nulos identificados!!!");
			return;
		}
		String senha = txtPassCCcp.getText();
		double valor = Double.parseDouble(valorTranferenciaentreCcCp.getText());
		if (cCorrente.equals("") || cPoupanca.equals("") || senha.equals("")) {
			alerta("Error", "Preencha os campos!", "Campo nulos identificados!!!");
		} else {
			ContaCorrente cC = new ContaCorrente();
			ContaPoupanca cP = new ContaPoupanca();
			ContaPoupancaDAO cpDao = new ContaPoupancaDAO();
			ContaCorrenteDAO ccDao = new ContaCorrenteDAO();
			cC = ccDao.findByNumeroConta(cCorrente);
			cP = cpDao.findByConta(cPoupanca);
			if (cC == null && cP == null) {

				alerta("Error!!!", "Tranferencia falhou!", "Uma das contas informada � invalida!");

			} else {
				if (cpDao.autentica(cPoupanca, senha)) {
					System.out.println("Autenticado com sucesso!!!");
					if (cC == null) {
						alerta("Error!!!", "Tranferencia falhou!", "A conta destino n�o existe.");
					} else {
						if (cP.getSaldo() < valor) {
							alerta("Aten��o!!!", "Tranferencia falhou!", "O saldo � insuficiente para a transferencia.");

						} else {
							try {
								cpDao.sacarCP(cP, cpDao.tarifaTransferencia(valor));
								ccDao.depositaCC(cC, valor);
								alerta("Sucesso!!!", "Tranferencia realizada com sucesso!",
										"Tudo certo, o dinheiro est� a caminho do destinatario!");
								FXMLLoader fxmlLoader = new FXMLLoader(
										TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));
								Parent root1 = fxmlLoader.load();
								Stage stage = new Stage();
								stage.setScene(new Scene(root1));
								stage.initStyle(StageStyle.UNDECORATED);
								stage.show();
								fecharTela(btnTransfereCcCp);
							} catch (IOException e) {

							}

						}

					}

				} else {
					alerta("Error!!!", "Autentica��o falhou!", "A senha ou sua conta n�o existem.");
				}
			}
		}
	
	
	}

	@FXML
	void voltarInicio(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(TelaPrincipalController.class.getResource("TelaPrincipal.fxml"));
			Parent root1 = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
			fecharTela(btnSair);

		} catch (IOException e) {

		}

	}

	// UTIL

	private void alerta(String title, String text, String header) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();

	}

	public void fecharTela(Button btnFechar) {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}

}
