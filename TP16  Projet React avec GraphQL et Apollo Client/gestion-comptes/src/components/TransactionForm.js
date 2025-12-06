import React, { useState } from 'react';
import { useMutation, useQuery } from '@apollo/client';
import { ADD_TRANSACTION } from '../graphql/mutations';
import { GET_ALL_COMPTES } from '../graphql/queries';

const TransactionForm = () => {
  const [montant, setMontant] = useState('');
  const [type, setType] = useState('DEPOT');
  const [compteId, setCompteId] = useState('');
  
  const { data: comptesData } = useQuery(GET_ALL_COMPTES);
  const [addTransaction] = useMutation(ADD_TRANSACTION);
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addTransaction({
        variables: {
          transactionRequest: {
            type,
            montant: parseFloat(montant),
            compteId,
          },
        },
      });
      setMontant('');
      setType('DEPOT');
      setCompteId('');
    } catch (error) {
      console.error('Erreur lors de l\'ajout de la transaction :', error);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <h2>Ajouter une Transaction</h2>
      <label>
        Compte :
        <select
          value={compteId}
          onChange={(e) => setCompteId(e.target.value)}
          required>
          <option value="">Sélectionnez un compte</option>
          {comptesData?.allComptes.map((compte) => (
            <option key={compte.id} value={compte.id}>
              {compte.type} - Solde: {compte.solde}€
            </option>
          ))}
        </select>
      </label>
      <br />
      <label>
        Type :
        <select
          value={type}
          onChange={(e) => setType(e.target.value)}
          required>
          <option value="DEPOT">Dépôt</option>
          <option value="RETRAIT">Retrait</option>
        </select>
      </label>
      <br />
      <label>
        Montant :
        <input
          type="number"
          value={montant}
          onChange={(e) => setMontant(e.target.value)}
          required
          placeholder="Entrez le montant" />
      </label>
      <br />
      <button type="submit">Ajouter la transaction</button>
    </form>
  );
};

export default TransactionForm;
