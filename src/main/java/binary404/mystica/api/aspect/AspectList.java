package binary404.mystica.api.aspect;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AspectList implements Serializable {

    public LinkedHashMap<Aspect, Integer> aspects = new LinkedHashMap<>();

/*
    public AspectList(ItemStack stack) {
        try {
            AspectList temp = AspectHelper.getObjectAspects(stack);
            if (temp != null)
                for (Aspect tag : temp.getAspects()) {
                    add(tag, temp.getAmount(tag));
                }
        } catch (Exception exception) {
        }
    }
    */


    public AspectList() {
    }

    public AspectList copy() {
        AspectList out = new AspectList();
        for (Aspect a : getAspects())
            out.add(a, getAmount(a));
        return out;
    }


    public int size() {
        return this.aspects.size();
    }


    public int visSize() {
        int q = 0;

        for (Aspect as : this.aspects.keySet()) {
            q += getAmount(as);
        }

        return q;
    }


    public Aspect[] getAspects() {
        return (Aspect[]) this.aspects.keySet().toArray((Object[]) new Aspect[0]);
    }


    public Aspect[] getAspectsSortedByName() {
        try {
            Aspect[] out = (Aspect[]) this.aspects.keySet().toArray((Object[]) new Aspect[0]);
            boolean change = false;
            do {
                change = false;
                for (int a = 0; a < out.length - 1; a++) {
                    Aspect e1 = out[a];
                    Aspect e2 = out[a + 1];
                    if (e1 != null && e2 != null && e1.getTag().compareTo(e2.getTag()) > 0) {
                        out[a] = e2;
                        out[a + 1] = e1;
                        change = true;
                        break;
                    }
                }
            } while (change == true);
            return out;
        } catch (Exception e) {
            return getAspects();
        }
    }


    public Aspect[] getAspectsSortedByAmount() {
        try {
            Aspect[] out = (Aspect[]) this.aspects.keySet().toArray((Object[]) new Aspect[0]);
            boolean change = false;
            do {
                change = false;
                for (int a = 0; a < out.length - 1; a++) {
                    int e1 = getAmount(out[a]);
                    int e2 = getAmount(out[a + 1]);
                    if (e1 > 0 && e2 > 0 && e2 > e1) {
                        Aspect ea = out[a];
                        Aspect eb = out[a + 1];
                        out[a] = eb;
                        out[a + 1] = ea;
                        change = true;
                        break;
                    }
                }
            } while (change == true);
            return out;
        } catch (Exception e) {
            return getAspects();
        }
    }


    public int getAmount(Aspect key) {
        return (this.aspects.get(key) == null) ? 0 : ((Integer) this.aspects.get(key)).intValue();
    }


    public boolean reduce(Aspect key, int amount) {
        if (getAmount(key) >= amount) {
            int am = getAmount(key) - amount;
            this.aspects.put(key, Integer.valueOf(am));
            return true;
        }
        return false;
    }


    public AspectList remove(Aspect key, int amount) {
        int am = getAmount(key) - amount;
        if (am <= 0) {
            this.aspects.remove(key);
        } else {
            this.aspects.put(key, Integer.valueOf(am));
        }
        return this;
    }


    public AspectList remove(Aspect key) {
        this.aspects.remove(key);
        return this;
    }


    public AspectList add(Aspect aspect, int amount) {
        if (this.aspects.containsKey(aspect)) {
            int oldamount = ((Integer) this.aspects.get(aspect)).intValue();
            amount += oldamount;
        }
        this.aspects.put(aspect, Integer.valueOf(amount));
        return this;
    }


    public AspectList merge(Aspect aspect, int amount) {
        if (this.aspects.containsKey(aspect)) {
            int oldamount = ((Integer) this.aspects.get(aspect)).intValue();
            if (amount < oldamount) amount = oldamount;

        }
        this.aspects.put(aspect, Integer.valueOf(amount));
        return this;
    }

    public AspectList add(AspectList in) {
        for (Aspect a : in.getAspects())
            add(a, in.getAmount(a));
        return this;
    }

    public AspectList remove(AspectList in) {
        for (Aspect a : in.getAspects())
            remove(a, in.getAmount(a));
        return this;
    }

    public AspectList merge(AspectList in) {
        for (Aspect a : in.getAspects())
            merge(a, in.getAmount(a));
        return this;
    }


    public void readFromNBT(CompoundNBT nbttagcompound) {
        this.aspects.clear();
        ListNBT tlist = nbttagcompound.getList("Aspects", 10);
        for (int j = 0; j < tlist.size(); j++) {
            CompoundNBT rs = tlist.getCompound(j);
            if (rs.contains("key")) {
                add(AspectRegistry.getAspect(rs.getString("key")), rs
                        .getInt("amount"));
            }
        }
    }


    public void readFromNBT(CompoundNBT nbttagcompound, String label) {
        this.aspects.clear();
        ListNBT tlist = nbttagcompound.getList(label, 10);
        for (int j = 0; j < tlist.size(); j++) {
            CompoundNBT rs = tlist.getCompound(j);
            if (rs.contains("key")) {
                add(AspectRegistry.getAspect(rs.getString("key")), rs
                        .getInt("amount"));
            }
        }
    }


    public void writeToNBT(CompoundNBT nbttagcompound) {
        ListNBT tlist = new ListNBT();
        nbttagcompound.put("Aspects", tlist);
        for (Aspect aspect : getAspects()) {
            if (aspect != null) {
                CompoundNBT f = new CompoundNBT();
                f.putString("key", aspect.getTag());
                f.putInt("amount", getAmount(aspect));
                tlist.add(f);
            }
        }
    }

    public void writeToNBT(CompoundNBT nbttagcompound, String label) {
        ListNBT tlist = new ListNBT();
        nbttagcompound.put(label, tlist);
        for (Aspect aspect : getAspects()) {
            if (aspect != null) {
                CompoundNBT f = new CompoundNBT();
                f.putString("key", aspect.getTag());
                f.putInt("amount", getAmount(aspect));
                tlist.add(f);
            }
        }
    }

}
