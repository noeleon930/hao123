import csv 
import os,sys

#def isfind():


if __name__ == '__main__':
	

	os.chdir(os.getcwd()+'/train')

	f = open('log_train.csv', 'r') 
	csv_f = csv.reader(f)
	csv_f = list(csv_f)
	user_id = []

	for row in csv_f:
		user_id.append(row[0])

	user_id = list(set(user_id))
	access = [[int(a),0,0,0,0,0,0,0] for a in user_id]
	IndexofUser = 0
	for row in csv_f:
		IndexofUser = user_id.index(row[0])
		if(row[3]=='problem'):
			access[IndexofUser][1]+=1
		elif(row[3]=='video'):
			access[IndexofUser][2]+=1
		elif(row[3]=='access'):
			access[IndexofUser][3] +=1
		elif(row[3]=='wiki'):
			access[IndexofUser][4]+=1
		elif(row[3]=='discussion'):
			access[IndexofUser][5]+=1
		elif(row[3]=='nagivate' or row[3]=='navigate'):
			access[IndexofUser][6]+=1
		elif(row[3]=='page_close'):
			access[IndexofUser][7]+=1

		print access[IndexofUser]
	access.sort()
	print access
	
	fout = open('event_train.csv','w')
	fout.write('user_id,problem,video,access,wiki,discussion,nagivate,page_close\n')
	i=0
	for everyline in access:
		while(i<7):
			fout.write(str(everyline[i]))
			fout.write(',')
			i+=1
		fout.write(str(everyline[7]))
		i=0
		fout.write('\n')
	
