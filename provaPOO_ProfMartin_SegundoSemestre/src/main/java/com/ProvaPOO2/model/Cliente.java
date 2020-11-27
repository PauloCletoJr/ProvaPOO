package com.ProvaPOO2.model;

import com.Cliente.Cliente2;

public class Cliente {

	private int id;
	private String nome;
	private String cpf;
	private String profissao;
	private String email;
	private String endereco;
	private String telefone;
	private String status = "Inativo";


	public Cliente(String nome, String cpf, String profissao, String email, String endereco, String telefone) {		
		this.nome = nome;
		this.cpf = cpf;
		this.profissao = profissao;
		this.email = email;
		this.endereco = endereco;
		this.telefone = telefone;
	}



	public Cliente(Cliente2 cliente2) {
		this.nome = cliente2.getNome();
		this.cpf = cliente2.getCpf();
		this.profissao = cliente2.getProfissao();
		this.email = cliente2.getEmail();
		this.endereco = cliente2.getEndereco();
		this.telefone = cliente2.getTelefone();
		
	}
	public Cliente() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
