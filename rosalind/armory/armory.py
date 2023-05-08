# !pip install Bio

# GenBank Introduction
# https://rosalind.info/problems/gbk/

from sys import argv
from Bio import Entrez


def search_entrez(gene, start_date, end_date):
    organism = '"{0}"[Organism]'.format(gene)
    date_field = '"{0}"[Publication Date]'
    start_date_field = date_field.format(start_date)
    end_date_field = date_field.format(end_date)
    date_span = '{0} : {1}'.format(start_date_field, end_date_field)
    term = '{0} AND {1}'.format(organism, date_span)
    handle = Entrez.esearch(db="nucleotide", term=term)
    record = Entrez.read(handle)
    return record["Count"]


with open("./rosalind_gen_bank.txt", 'r') as inf:
  data = inf.readlines()
  gene = data[0].strip()
  start_date = data[1].strip()
  end_date = data[2].strip()
  print(search_entrez(gene, start_date, end_date))


# Data Formats
# https://rosalind.info/problems/frmt/

from sys import argv
from Bio import Entrez
from Bio import SeqIO

def get_nuc_fasta(nucleotide_ids):
    handle = Entrez.efetch(db="nucleotide", id=[nucleotide_ids], rettype="fasta")
    records = list(SeqIO.parse(handle, "fasta"))

    shortest = min(records, key=lambda x: len(x.seq))
    return shortest.format('fasta')



with open("rosalind_data_formats.txt", 'r') as inf:
  data = inf.read()
  nucleotide_ids = ', '.join(data.strip().split())
  print(get_nuc_fasta(nucleotide_ids))