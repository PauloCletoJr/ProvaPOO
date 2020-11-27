package com.ProvaPOO2.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.Cliente.Cliente2;
import com.ProvaPOO2.DAO.ClienteDAO;
import com.ProvaPOO2.model.Cliente;
import com.ProvaPOO2.util.TextFieldFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class ListaClientesController {

	@FXML
	private TableView<Cliente2> tvClientes;

	@FXML
	private Button btnEditarCliente;

	@FXML
	private Button btnExcluir;

	@FXML
	private Button btnAtualizar;

	@FXML
	private TextField txtBuscarCPF;

	@FXML
	private TableColumn<Cliente2, String> columnNome;

	@FXML
	private TableColumn<Cliente2, String> columnCpf;

	@FXML
	private TableColumn<Cliente2, String> columnProfissao;

	@FXML
	private TableColumn<Cliente2, String> columnEmail;

	@FXML
	private TableColumn<Cliente2, String> columnEndereco;

	@FXML
	private TableColumn<Cliente2, String> columnTelefone;

	@FXML
	private TableColumn<Cliente2, String> columnStatus;

	@FXML
	private Button btnPesquisar;

	@FXML
	private TextField tfNome;

	@FXML
	private TextField tfCpf;

	@FXML
	private TextField tfProfissao;

	@FXML
	private TextField tfEmail;

	@FXML
	private TextField tfEndereco;

	@FXML
	private TextField tfTelefone;

	@FXML
	private Label lblNome;

	@FXML
	private Label lblcpf;

	@FXML
	private Label lblprofissao;

	@FXML
	private Label lblemail;

	@FXML
	private Label lblendreco;

	@FXML
	private Label lbltelefone;

	@FXML
	private Button btnConfimarEdicao;
    @FXML
    void tfMaskCpf() {
    	TextFieldFormatter tff = new TextFieldFormatter();
    	tff.setMask("###.###.###-##");
    	tff.setCaracteresValidos("0123456789");
    	tff.setTf(txtBuscarCPF);
    	tff.formatter();

    }

	@FXML
	void tfMaskTelefone() {
		TextFieldFormatter tff = new TextFieldFormatter();
		tff.setMask("(##)#####-####");
		tff.setCaracteresValidos("0123456789");
		tff.setTf(tfTelefone);
		tff.formatter();
	}

	private ClienteDAO cliDao = new ClienteDAO();
	private List<Cliente> clienteList = cliDao.listarClientes();
	private ObservableList<Cliente2> listTabelaCliente = FXCollections.observableArrayList();

	@FXML
	public void pesquisarCliente(ActionEvent event) {
		String cpf = txtBuscarCPF.getText();
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		Cliente cliente = cliDao.buscaClienteByCPF(cpf);
		if (!listTabelaCliente.isEmpty()) {
			listTabelaCliente.clear();
		}
		Cliente2 cli = new Cliente2(cliente.getId(), cliente.getNome(), cliente.getCpf(),
				cliente.getProfissao(), cliente.getEmail(), cliente.getEndereco(), cliente.getTelefone(),
				cliente.getStatus());
		listTabelaCliente.add(cli);

		columnNome.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Nome"));
		columnCpf.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Cpf"));
		columnProfissao.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Profissao"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Email"));
		columnEndereco.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Endereco"));
		columnTelefone.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("telefone"));
		columnStatus.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Status"));

		tvClientes.setItems(listTabelaCliente);

	}

	public void chamarEditar() {
		Cliente2 cliente2 = tvClientes.getSelectionModel().getSelectedItem();
		tfNome.setText(cliente2.getNome());
		tfCpf.setText(cliente2.getCpf());
		tfCpf.setEditable(false);
		tfEmail.setText(cliente2.getEmail());
		tfEndereco.setText(cliente2.getEndereco());
		tfProfissao.setText(cliente2.getProfissao());
		tfTelefone.setText(cliente2.getTelefone());

	}

	public void editar() {
		Cliente cliente = new Cliente();
		cliente.setNome(tfNome.getText());
		cliente.setCpf(tfCpf.getText());
		cliente.setTelefone(tfTelefone.getText());
		cliente.setEmail(tfEmail.getText());
		cliente.setProfissao(tfProfissao.getText());
		cliente.setEndereco(tfEndereco.getText());

		cliDao.alterarCliente(cliente);
		clienteList = cliDao.listarClientes();
		listarClientes();

	}

	public void listarClientes() {
		if (!listTabelaCliente.isEmpty()) {
			listTabelaCliente.clear();
		}
		for (Cliente cliente : clienteList) {
			Cliente2 cli = new Cliente2(cliente.getId(), cliente.getNome(), cliente.getCpf(),
					cliente.getProfissao(), cliente.getEmail(), cliente.getEndereco(), cliente.getTelefone(),
					cliente.getStatus());
			if (cliente.getStatus().equals("ATIVO")) {
				listTabelaCliente.add(cli);
			}
		}

		columnNome.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Nome"));
		columnCpf.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Cpf"));
		columnProfissao.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Profissao"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Email"));
		columnEndereco.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Endereco"));
		columnTelefone.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("telefone"));
		columnStatus.setCellValueFactory(new PropertyValueFactory<Cliente2, String>("Status"));

		tvClientes.setItems(listTabelaCliente);
	}



	public void excluir() {
		Cliente2 cliente = tvClientes.getSelectionModel().getSelectedItem();
		String cpf = cliente.getCpf();
		listTabelaCliente.remove(cliente);
		cliDao.removeCliente(cpf);
		clienteList = cliDao.listarClientes();
		listarClientes();

	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		listarClientes();

	}
	
	


}
