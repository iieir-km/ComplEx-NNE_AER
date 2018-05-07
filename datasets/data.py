
# coding: utf-8

# In[3]:


def ent_rel2id(dataset):
    ent_set = set()
    rel_set = set()
    with open(dataset+'/_train.txt') as f:
        for line in f:
            h,r,t = line.split()
            ent_set.add(h)
            ent_set.add(t)
            rel_set.add(r)
    with open(dataset+'/entities.txt','w') as f:
        f.write('\n'.join(ent_set))
    with open(dataset+'/relations.txt','w') as f:
        f.write('\n'.join(rel_set))


# In[7]:


def translate_triple(dataset):
    ent2id = {}
    rel2id = {}
    with open(dataset+'/entities.txt') as f:
        for i,line in enumerate(f):
            ent2id[line.strip()] = i
    with open(dataset+'/relations.txt') as f:
        for i,line in enumerate(f):
            rel2id[line.strip()] = i
    with open(dataset+'/_train.txt') as f, open(dataset+'/train.txt','w') as out:
        for line in f:
            h,r,t = line.split()
            out.write('%s\t%s\t%s\n' % (ent2id[h],rel2id[r],ent2id[t]))
    with open(dataset+'/_valid.txt') as f, open(dataset+'/valid.txt','w') as out:
        for line in f:
            h,r,t = line.split()
            out.write('%s\t%s\t%s\n' % (ent2id[h],rel2id[r],ent2id[t]))
            
    with open(dataset+'/_test.txt') as f, open(dataset+'/test.txt','w') as out:
        for line in f:
            h,r,t = line.split()
            out.write('%s\t%s\t%s\n' % (ent2id[h],rel2id[r],ent2id[t]))


# In[11]:


def write_all(dataset):
    train = []
    valid = []
    test = []
    with open(dataset+'/train.txt') as f:
        for line in f:
            h,r,t = line.split()
            train.append((h,r,t))
    with open(dataset+'/valid.txt') as f:
        for line in f:
            h,r,t  = line.split()
            valid.append((h,r,t))
    with open(dataset+'/test.txt') as f:
        for line in f:
            h,r,t = line.split()
            test.append((h,r,t))
    _all = train + valid + test
    with open(dataset+'/all.txt','w') as f:
        for h,r,t in _all:
            f.write('%s\t%s\t%s\n' % (h,r,t))        


# In[21]:


def translate_cons(dataset):
    rel2id = {}
    with open(dataset+'/relations.txt') as f:
        for i,line in enumerate(f):
            rel2id[line.strip()] = i
    with open(dataset+'/_cons.txt') as f,open(dataset+'/cons.txt','w') as out:
        for line in f:
            rule_str, conf = line.strip().split()
            body,head = rule_str.split(',')
            prefix = ''
            if '-' in body:
                prefix = '-'
                body = body[1:]
            rule = prefix + str(rel2id[body])+','+str(rel2id[head])
            out.write('%s\t%s\n' % (rule,conf))

if __name__ == "__main__":
    #ent_rel2id('FB15K')
    #ent_rel2id('WN18')
    ent_rel2id('DB100K')
    #translate_triple('FB15K')
    #translate_triple('WN18')
    translate_triple('DB100K')
    #write_all('FB15K')
    #write_all('WN18')
    write_all('DB100K')
    #translate_cons('FB15K')
    #translate_cons('WN18')
    translate_cons('DB100K')